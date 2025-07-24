namespace Bl.Fat.Sample.Android.Api.Model;
public class UserModel
{
    public long Id { get; set; }
    public string? Name { get; set; }
    public string? Email { get; set; }
    public string? Password { get; set; }
    public string? PhoneNumber { get; set; }
    public string? Address { get; set; }
    public DateTimeOffset CreatedAt { get; set; } = DateTimeOffset.UtcNow;
    public DateTimeOffset UpdatedAt { get; set; } = DateTimeOffset.UtcNow;
}
