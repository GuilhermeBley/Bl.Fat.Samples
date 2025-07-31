package com.example.blfatsamples.model;

public class ProductModel {
    private String Name;
    private  String Description;
    private  String ImgUrl;
    private  String Category;
    private  double Price;

    public ProductModel(String name, String description, String imgUrl, String category, double price) {
        Name = name;
        Description = description;
        ImgUrl = imgUrl;
        Price = price;
        Category = category;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }
}
