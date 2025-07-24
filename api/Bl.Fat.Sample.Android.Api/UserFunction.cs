using Bl.Fat.Sample.Android.Api.Model;
using Bl.Fat.Sample.Android.Api.Repositories;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.Functions.Worker;
using Microsoft.Extensions.Logging;
using System.Text.Json;
using System.Text;

namespace Bl.Fat.Sample.Android.Api;

public class UserFunction
{
    private readonly ILogger<UserFunction> _logger;
    private readonly MenuContext _menuContext;

    public UserFunction(
        ILogger<UserFunction> logger,
        MenuContext menuContext)
    {
        _logger = logger;
        _menuContext = menuContext;
    }

    [Function("UserFunctionCreateUser")]
    public async Task<IActionResult> CreateUserAsync(
        [HttpTrigger(AuthorizationLevel.Anonymous, "post", Route = "api/user")] HttpRequest req)
    {
        try
        {
            // Read request body
            string requestBody = await new StreamReader(req.Body).ReadToEndAsync();
            var userInput = JsonSerializer.Deserialize<UserModel>(requestBody);

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
                Address = userInput.Address,
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
        [HttpTrigger(AuthorizationLevel.Anonymous, "post", Route = "api/user/login")] HttpRequest req)
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
                    user.CreatedAt
                } 
            });
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error during login");
            return new StatusCodeResult(StatusCodes.Status500InternalServerError);
        }
    }
}

// Helper class for login request
public class LoginRequest
{
    public string Email { get; set; }
    public string Password { get; set; }
}