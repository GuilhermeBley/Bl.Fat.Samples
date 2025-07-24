namespace Bl.Fat.Sample.Android.Api.Model;

public class ProductModel
{
    public long Id { get; set; }
    public string Name { get; set; } = string.Empty;
    public string? Description { get; set; }
    public decimal Price { get; set; }
    public string Category { get; set; } = string.Empty;
    public string? ImageUrl { get; set; }
    public DateTimeOffset CreatedAt { get; set; } = DateTimeOffset.UtcNow;
}
