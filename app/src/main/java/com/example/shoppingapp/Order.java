package com.example.shoppingapp;

public class Order {

    private int id;
    private String date;
    private double total;
    private String paymentMethod;

    // Constructor WITH payment
    public Order(int id, String date, double total, String paymentMethod) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.paymentMethod = paymentMethod;
    }

    // Optional constructor (if needed somewhere)
    public Order(int id, String date, double total) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.paymentMethod = "Cash on Delivery";
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
