package com.supercon.service;

import com.supercon.model.Order;
import com.supercon.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {

    private ShoppingCart shoppingCart;

    public Optional<Order> showConfirmation(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
        Order order = new Order(shoppingCart.getCustomer(), shoppingCart.getProducts(),
                shoppingCart.getLoyaltyPointsEarned(), shoppingCart.getTotalPrice(), "ORDER_CONFIRMED");
        return shoppingCart.getProducts().size() > 0 ?
                Optional.of(order) : Optional.empty();
    }

    public Optional<Order> placeOrder(Order order) {
        double totalPrice = order.getTotalPrice();

        totalPrice = totalPrice > Double.valueOf(1000) ?
                calculateDiscount(Double.valueOf(.1), totalPrice) : totalPrice;
        return order.getProducts().size() > 0 ?
                Optional.of(new Order(order.getCustomer(), order.getProducts(),
                        order.getLoyaltyPointsEarned(), totalPrice, "ORDER_PLACED")) : Optional.empty();
    }

    private double calculateDiscount(double discount, double totalPrice){
        discount = (totalPrice * discount);
        totalPrice = totalPrice - discount;
        return totalPrice;
    }

}
