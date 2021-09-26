package com.example.hotstuffpizza.models;

import java.util.Date;

public class Order {
    private String id;
    private String userId;
    private String name;
    private String email;
    private double total;
    private boolean isComplete;
    private String cardHoldersName;
    private String paymentMethod;
    private String cardNumber;
    private String expirationDate;
    private String deliveryMethod;
    private boolean paymentConfirm;

    public Order() {
    }

    public Order(String userId, String name, String email, double total, boolean isComplete) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.total = total;
        this.isComplete = isComplete;
    }

    public Order(String id, String userId, String name, String email, double total, String cardHoldersName, String paymentMethod, String cardNumber, String expirationDate, String deliveryMethod, boolean paymentConfirm, boolean isComplete) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.total = total;
        this.isComplete = isComplete;
        this.cardHoldersName = cardHoldersName;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.deliveryMethod = deliveryMethod;
        this.paymentConfirm = paymentConfirm;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public String getCardHoldersName() {
        return cardHoldersName;
    }

    public void setCardHoldersName(String cardHoldersName) {
        this.cardHoldersName = cardHoldersName;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public boolean isPaymentConfirm() {
        return paymentConfirm;
    }

    public void setPaymentConfirm(boolean paymentConfirm) {
        this.paymentConfirm = paymentConfirm;
    }
}
