using System.Security.Claims;

namespace Bl.Fat.Sample.Android.Api.Extensions;
internal static class ClaimExtension
{
    public static long? GetUserIdOrDefault(this ClaimsPrincipal user)
    {
        if (user == null) throw new ArgumentNullException(nameof(user));
        var userIdClaim = user.FindFirst(ClaimTypes.NameIdentifier);
        if (userIdClaim == null || !long.TryParse(userIdClaim.Value, out var userId))
        {
            return null;
        }
        return userId;
    }

    public static string? GetUserNameOrDefault(this ClaimsPrincipal user)
    {
        if (user == null) throw new ArgumentNullException(nameof(user));
        var userNameClaim = user.FindFirst(ClaimTypes.Name);
        if (userNameClaim == null)
        {
            return null;
        }
        return userNameClaim.Value;
    }

    public static string? GetUserEmailOrDefault(this ClaimsPrincipal user)
    {
        if (user == null) throw new ArgumentNullException(nameof(user));
        var userEmailClaim = user.FindFirst(ClaimTypes.Email);
        if (userEmailClaim == null)
        {
            return null;
        }
        return userEmailClaim.Value;
    }
}
