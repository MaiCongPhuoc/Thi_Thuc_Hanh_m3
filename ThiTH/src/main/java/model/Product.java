package model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Product {
    private int id;
    private String productName;
    private double price;
    private int quantity;
    private String color;
    private String description;
    private int idcategory;

    public Product() {
    }

    public Product(int id, String productName, double price, int quantity, String color, String description, int idcategory) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.color = color;
        this.description = description;
        this.idcategory = idcategory;
    }

    @NotEmpty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public int getIdcategory() {
        return idcategory;
    }

    public void setIdcategory(int idcategory) {
        this.idcategory = idcategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NotEmpty
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @NotNull
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NotNull
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NotEmpty
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
