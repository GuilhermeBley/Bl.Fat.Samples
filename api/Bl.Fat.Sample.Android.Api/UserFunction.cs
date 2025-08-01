using Bl.Fat.Sample.Android.Api.Model;
using Bl.Fat.Sample.Android.Api.Repositories;
using Bl.Fat.Sample.Android.Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.Functions.Worker;
using Microsoft.Extensions.Logging;
using System.ComponentModel.DataAnnotations;
using System.Text;
using System.Text.Json;
using System.Text.Json.Nodes;

namespace Bl.Fat.Sample.Android.Api;

public class UserFunction
{
    private readonly ILogger<UserFunction> _logger;
    private readonly MenuContext _menuContext;
    private readonly BlAuthenticationService _auth;

    public UserFunction(
        ILogger<UserFunction> logger,
        MenuContext menuContext,
        BlAuthenticationService auth)
    {
        _logger = logger;
        _menuContext = menuContext;
        _auth = auth;
    }

    [Function("UserFunctionCreateUser")]
    public async Task<IActionResult> CreateUserAsync(
        [HttpTrigger(AuthorizationLevel.Anonymous, "post", Route = "user")] HttpRequest req)
    {
        try
        {
            // Read request body
            string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
            var userInput = JsonSerializer.Deserialize<CreateUserModel>(requestBody);

            if (userInput == null || string.IsNullOrEmpty(userInput.Email) || string.IsNullOrEmpty(userInput.Password))
            {
                return new BadRequestObjectResult("Email and password are required.");
            }

            // Check if user already exists
            var existingUser = await _menuContext.Users.FirstOrDefaultAsync(u => u.Email == userInput.Email);
            if (existingUser != null)
            {
                return new ConflictObjectResult("User with this email already exists.");
            }

            // Create new user
            var newUser = new UserModel
            {
                Name = userInput.Name,
                Email = userInput.Email,
                Password = userInput.Password, // In production, you should hash the password
                Address = userInput.Address is null || userInput.Address is string ? null : JsonSerializer.Serialize(userInput.Address),
                PhoneNumber = userInput.PhoneNumber,
                CreatedAt = DateTime.UtcNow
            };

            await _menuContext.Users.AddAsync(newUser);
            await _menuContext.SaveChangesAsync();

            return new OkObjectResult(new { Message = "User created successfully", UserId = newUser.Id });
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error creating user");
            return new StatusCodeResult(StatusCodes.Status500InternalServerError);
        }
    }

    [Function("UserFunctionLoginUserAsync")]
    public async Task<IActionResult> LoginUserAsync(
        [HttpTrigger(AuthorizationLevel.Anonymous, "post", Route = "user/login")] HttpRequest req)
    {
        try
        {
            // Read request body
            string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
            var loginRequest = JsonSerializer.Deserialize<LoginRequest>(requestBody);

            if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Email) || string.IsNullOrEmpty(loginRequest.Password))
            {
                return new BadRequestObjectResult("Email and password are required.");
            }

            // Find user by email
            var user = await _menuContext.Users.FirstOrDefaultAsync(u => u.Email == loginRequest.Email);
            if (user == null)
            {
                return new UnauthorizedObjectResult("Invalid email or password.");
            }

            // Verify password (in production, use proper password hashing)
            if (user.Password != loginRequest.Password)
            {
                return new UnauthorizedObjectResult("Invalid email or password.");
            }

            return new OkObjectResult(new { 
                Message = "Login successful", 
                User = new 
                { 
                    user.Name,
                    user.Email,
                    user.PhoneNumber,
                    user.Address,
                    user.Id,
                    user.CreatedAt,
                    Token = _auth.GenerateToken(user.Id, user.Name, user.Email)
                } 
            });
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error during login");
            return new StatusCodeResult(StatusCodes.Status500InternalServerError);
        }
    }

    [Function("UserFunctionGetUserByIdAsync")]
    public async Task<IActionResult> GetUserByIdAsync(
        [HttpTrigger(AuthorizationLevel.Anonymous, "get", Route = "user/id")] HttpRequest req)
    {
        try
        {
            var claims = _auth.GetClaims(req);
            var userId = claims.GetUserIdOrDefault();
            if (userId is null)  return new UnauthorizedResult();

            var userInfo = await _menuContext
                .Users
                .AsNoTracking()
                .Where(u => u.Id == userId)
                .Select(x => new
                {
                    x.Id,
                    x.Name,
                    x.Email,
                    x.PhoneNumber,
                    Address = GetAddressObj(x.Address),
                    x.CreatedAt
                })
                .FirstOrDefaultAsync();

            return new OkObjectResult(new
            {
                Message = "User retrieved successfully",
                User = userInfo
            });
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error during login");
            return new StatusCodeResult(StatusCodes.Status500InternalServerError);
        }
    }

    [Function("UserFunctionUpdateUserAsync")]
    public async Task<IActionResult> UpdateUserAsync(
    [HttpTrigger(AuthorizationLevel.Anonymous, "put", Route = "user")] HttpRequest req)
    {
        try
        {
            var claims = _auth.GetClaims(req);
            var userId = claims.GetUserIdOrDefault();
            if (userId is null)
                return new UnauthorizedResult();

            string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
            var updateModel = JsonSerializer.Deserialize<UpdateUserModel>(requestBody, new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            });

            if (updateModel == null || string.IsNullOrWhiteSpace(updateModel.Email) || string.IsNullOrWhiteSpace(updateModel.Name))
            {
                return new BadRequestObjectResult("Name and email are required.");
            }

            var user = await _menuContext.Users.FirstOrDefaultAsync(u => u.Id == userId);
            if (user == null)
                return new NotFoundObjectResult("User not found.");

            // Update fields
            user.Name = updateModel.Name;
            user.Email = updateModel.Email;
            user.PhoneNumber = updateModel.PhoneNumber;
            user.Address = updateModel.Address is null || updateModel.Address is string
                ? null
                : JsonSerializer.Serialize(updateModel.Address);

            _menuContext.Users.Update(user);
            await _menuContext.SaveChangesAsync();

            return new OkObjectResult(new { Message = "User updated successfully" });
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error updating user");
            return new StatusCodeResult(StatusCodes.Status500InternalServerError);
        }
    }


    private static JsonNode? GetAddressObj(string? address)
    {
        if (string.IsNullOrEmpty(address)) return null;
        try
        {
            return JsonNode.Parse(address) ?? new JsonObject();
        }
        catch (JsonException)
        {
            return new JsonObject { ["error"] = "Invalid address format" };
        }
    }
}

// Helper class for login request
public class LoginRequest
{
    public string? Email { get; set; }
    public string? Password { get; set; }
}