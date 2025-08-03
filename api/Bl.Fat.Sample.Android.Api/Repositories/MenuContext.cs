using Bl.Fat.Sample.Android.Api.Model;
using Microsoft.Extensions.Configuration;

namespace Bl.Fat.Sample.Android.Api.Repositories;

public class MenuContext : DbContext
{
    private readonly string? _connectionString;

    public DbSet<ProductModel> Products { get; set; } = null!;
    public DbSet<UserModel> Users { get; set; } = null!;
    public DbSet<OrderModel> Orders { get; set; } = null!;

    public MenuContext(IConfiguration configuration)
    {
        _connectionString = configuration.GetConnectionString("dbase");
    }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        base.OnConfiguring(optionsBuilder);

        optionsBuilder.UseMySql(
            _connectionString,
            ServerVersion.Create(new Version(8, 0), Pomelo.EntityFrameworkCore.MySql.Infrastructure.ServerType.MySql));
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        base.OnModelCreating(modelBuilder);

        modelBuilder.Entity<ProductModel>(b =>
        {
            b.HasKey(p => p.Id);
            b.Property(p => p.Id).ValueGeneratedOnAdd();
            b.Property(p => p.Name).IsRequired().HasMaxLength(250);
            b.Property(p => p.Description).HasMaxLength(10_000);
            b.Property(p => p.Category).IsRequired().HasMaxLength(250);
            b.Property(p => p.ImageUrl).HasMaxLength(2500);
            b.Property(p => p.Price).HasColumnType("decimal(18,2)");
            b.Property(p => p.CreatedAt).HasColumnType("datetime");
        });

        modelBuilder.Entity<UserModel>(b =>
        {
            b.HasKey(p => p.Id);
            b.Property(p => p.Id).ValueGeneratedOnAdd();
            b.Property(p => p.Name).IsRequired().HasMaxLength(250);
            b.Property(p => p.Email).IsRequired().HasMaxLength(250);
            b.Property(p => p.Password).IsRequired().HasMaxLength(1000);
            b.Property(p => p.Address).HasMaxLength(1000);
            b.Property(p => p.PhoneNumber).HasMaxLength(20);
            b.Property(p => p.CreatedAt).HasColumnType("datetime");
            b.Property(p => p.UpdatedAt).HasColumnType("datetime");

            b.HasIndex(p => p.Email)
                .IsUnique()
                .HasDatabaseName("IX_Users_Email");
        });

        modelBuilder.Entity<OrderModel>(b =>
        {
            b.HasKey(p => p.Id);
            b.Property(p => p.Id).ValueGeneratedOnAdd();
            b.Property(p => p.TotalProductQuantity).IsRequired();
            b.Property(p => p.TotalValue).IsRequired();
            b.Property(p => p.ProductsJson).IsRequired().HasColumnType("TEXT");
            b.Property(p => p.CreatedAt).IsRequired();
            b.Property(p => p.DeliveredAt).IsRequired(false);
            b.Property(p => p.StartedDeliveringAt).IsRequired(false);
            b.Ignore(p => p.Products);
        });
    }
}
