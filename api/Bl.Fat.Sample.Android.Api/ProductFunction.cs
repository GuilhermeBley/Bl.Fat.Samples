using Bl.Fat.Sample.Android.Api.Repositories;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Azure.Functions.Worker;
using Microsoft.Extensions.Logging;

namespace Bl.Fat.Sample.Android.Api;

public class ProductFunction
{
    private readonly ILogger<UserFunction> _logger;
    private readonly MenuContext _menuContext;

    public ProductFunction(
        ILogger<UserFunction> logger,
        MenuContext menuContext)
    {
        _logger = logger;
        _menuContext = menuContext;
    }

    [Function("ProductFunctionGetAll")]
    public async Task<IActionResult> GetProductsAsync(
        [HttpTrigger(AuthorizationLevel.Anonymous, "get", Route = "product")] HttpRequest req)
    {
        var items = await _menuContext
            .Products
            .AsNoTracking()
            .Select(x => new
            {
                x.Id,
                x.Name,
                x.Description,
                x.CreatedAt,
                x.ImageUrl,
                x.Price,
                x.Category
            }).ToListAsync();

        if (items == null || !items.Any())
        {
            _logger.LogInformation("No products found.");
            return new NotFoundResult();
        }

        return new OkObjectResult(items);
    }
}
