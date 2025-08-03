namespace Bl.Fat.Sample.Android.Api.Model;

public class OrderModel
{
    public long Id { get; set; }
    public double TotalValue { get; set; }
    public int TotalProductQuantity { get; set; }
    public DateTime DeliveredAt { get; set; }
    public DateTime? StartedDeliveringAt { get; set; }
    public DateTime? CreatedAt { get; set; }
    public string? ProductsJson { get; set; }

    public IEnumerable<ProductModel> Products
    {
        get
        {
            if (string.IsNullOrEmpty(ProductsJson))
            {
                return Enumerable.Empty<ProductModel>();
            }
            try
            {
                return System.Text.Json.JsonSerializer.Deserialize<IEnumerable<ProductModel>>(ProductsJson) 
                    ?? Enumerable.Empty<ProductModel>();
            }
            catch (Exception)
            {
                return Enumerable.Empty<ProductModel>();
            }
        }
    }
}
