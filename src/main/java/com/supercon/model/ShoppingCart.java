package com.supercon.model;

import java.util.List;

public class ShoppingCart {

    private List<Product> products;
    private Customer customer;
    private int loyaltyPointsEarned;
    private double totalPrice;
    private String cartState;

    public ShoppingCart(Customer customer, List<Product> products, int loyaltyPointsEarned, double totalPrice, String cartState)  {
        this.customer = customer;
        this.products = products;
        this.loyaltyPointsEarned = loyaltyPointsEarned;
        this.totalPrice = totalPrice;
        this.cartState = cartState;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getLoyaltyPointsEarned() {
        return loyaltyPointsEarned;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getCartState() {
        return cartState;
    }

}
