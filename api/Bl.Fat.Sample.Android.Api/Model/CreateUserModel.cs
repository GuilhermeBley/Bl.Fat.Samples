using System.ComponentModel.DataAnnotations;

namespace Bl.Fat.Sample.Android.Api.Model;

public class CreateUserModel
{
    [Required]
    public string Name { get; set; } = string.Empty;
    [Required, EmailAddress]
    public string Email { get; set; } = string.Empty;
    [Required]
    public string Password { get; set; } = string.Empty;
    public string? PhoneNumber { get; set; }
    public string? Address { get; set; }
}
