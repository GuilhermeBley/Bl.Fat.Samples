using Microsoft.AspNetCore.Http;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;

namespace Bl.Fat.Sample.Android.Api.Services;

public class BlAuthenticationService
{
    private const string TokenKey = "açsldnmadnm21093u210ncoisncioas"; // TODO: it can be stored in a secure place like environment variables or a secrets manager

    public string GenerateToken(long id, string name, string email)
    {
        var tokenHandler = new JwtSecurityTokenHandler();
        var signingKey = GenerateSecurityToken();

        var claims = new List<Claim>
        {
            new Claim(ClaimTypes.NameIdentifier, id.ToString()),
            new Claim(ClaimTypes.Name, name),
            new Claim(ClaimTypes.Email, email)
        };

        var tokenDescriptor = new SecurityTokenDescriptor
        {
            Subject = new ClaimsIdentity(claims),
            Expires = DateTime.UtcNow.AddHours(12),
            SigningCredentials = new SigningCredentials(signingKey, SecurityAlgorithms.HmacSha256Signature),
        };

        var token = tokenHandler.CreateToken(tokenDescriptor);
        return tokenHandler.WriteToken(token);
    }

    public bool IsAuthenticated(string token)
    {
        var tokenHandler = new JwtSecurityTokenHandler();
        var signingKey = GenerateSecurityToken();
        try
        {
            if (!tokenHandler.CanReadToken(token)) return false;

            var result = tokenHandler.ValidateToken(token, new TokenValidationParameters
            {
                ValidateIssuer = false,
                ValidateAudience = false,
                ValidateIssuerSigningKey = false,
                IssuerSigningKey = signingKey,
                ValidateLifetime = true,
                // Set clock skew to zero so tokens expire exactly at token expiration time
                ClockSkew = TimeSpan.Zero
            }, out SecurityToken validatedToken);


            return true;
        }
        catch
        {
            return false;
        }
    }

    public ClaimsPrincipal GetClaims(HttpRequest request)
    {
        var authorizationToken = ((string?)request.Headers.Authorization.FirstOrDefault())?.Replace(
            "Bearer ", 
            string.Empty);

        if (string.IsNullOrEmpty(authorizationToken)) return new ClaimsPrincipal(); // not authenticated

        var tokenHandler = new JwtSecurityTokenHandler();
        var signingKey = GenerateSecurityToken();
        try
        {
            if (!tokenHandler.CanReadToken(authorizationToken)) return new ClaimsPrincipal(); // not authenticated

            var result = tokenHandler.ValidateToken(authorizationToken, new TokenValidationParameters
            {
                ValidateIssuer = false,
                ValidateAudience = false,
                ValidateIssuerSigningKey = false,
                IssuerSigningKey = signingKey,
                ValidateLifetime = true,
                // Set clock skew to zero so tokens expire exactly at token expiration time
                ClockSkew = TimeSpan.Zero
            }, out SecurityToken validatedToken);


            return result;
        }
        catch
        {
            return new ClaimsPrincipal(); // not authenticated
        }
    }

    private SymmetricSecurityKey GenerateSecurityToken()
    {
        return new SymmetricSecurityKey(System.Text.Encoding.UTF8.GetBytes(TokenKey));
    }
}
