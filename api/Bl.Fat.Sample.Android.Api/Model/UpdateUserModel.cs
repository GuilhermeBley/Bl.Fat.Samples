using System.ComponentModel.DataAnnotations;

namespace Bl.Fat.Sample.Android.Api.Model;


public class UpdateUserModel
{
    [Required]
    public string Name { get; set; } = string.Empty;
    [Required, EmailAddress]
    public string Email { get; set; } = string.Empty;
    public string? PhoneNumber { get; set; }
    public object? Address { get; set; } // Can be a JSON object or null
}