package com.supercon.model;

import java.util.List;

public class Order extends ShoppingCart {
    public Order(Customer customer, List<Product> products, int loyaltyPointsEarned, double totalPrice, String cartState) {
        super(customer, products, loyaltyPointsEarned, totalPrice, cartState);
    }

}
