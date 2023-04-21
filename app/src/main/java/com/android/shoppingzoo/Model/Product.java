package com.android.shoppingzoo.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    String productId;
    String name;
    String category;
    String manufacturer;
    String color;
    String stock;
    String photoUrl;
    String description;
    int quantityInCart;
    double price,avgStarRating;
    private ArrayList<Review> reviewArrayList;

    public Product() {
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public double getAvgStarRating() {
        return avgStarRating;
    }

    public void setAvgStarRating(double avgStarRating) {
        this.avgStarRating = avgStarRating;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }

    public void setReviewArrayList(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", color='" + color + '\'' +
                ", stock='" + stock + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", description='" + description + '\'' +
                ", quantityInCart=" + quantityInCart +
                ", price=" + price +
                ", avgStarRating=" + avgStarRating +
                ", reviewArrayList=" + reviewArrayList +
                '}';
    }
}
