package com.example.hotstuff;

public class add {
    private String price;
    private String description;
    private String subcategory;
    private String maincategory;

public add(){}
public add(String price,String description,String subcategory,String maincategory) {
        this.price = price;
        this.description=description;
        this.subcategory=subcategory;
        this.maincategory=maincategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }
    public String getMaincategory() {
        return maincategory;
    }

    public void setMaincategory(String maincategory) {
        this.maincategory = maincategory;
    }
}

