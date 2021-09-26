package com.example.hotstuffpizza.models;

public class Cart {
    private String id;
    private String userId;
    private String pizzaId;
    private String name;
    private String description;
    private double perPrice;
    private long quantity;

    public Cart() {
    }

    public Cart(String userId, String pizzaId, String name, String description, double perPrice, long quantity) {
        this.userId = userId;
        this.pizzaId = pizzaId;
        this.name = name;
        this.description = description;
        this.perPrice = perPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(String pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(double perPrice) {
        this.perPrice = perPrice;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
