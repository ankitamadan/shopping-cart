package com.supercon.service;

import com.supercon.model.Customer;
import com.supercon.model.Order;
import com.supercon.model.Product;
import com.supercon.model.ShoppingCart;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static com.supercon.testdata.TestDataFactory.PRODUCT_LIST;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

    private Order orderConfirmed;

    private Customer customer;

    private List<Product> products;

    private double totalPrice;

    private ShoppingCart shoppingCart;

    private int loyaltyPointsEarned;

    @InjectMocks
    private OrderService orderService;

    @Before
    public void setup(){
        customer = new Customer("Test Customer");
        products = PRODUCT_LIST;
        totalPrice = Double.valueOf(1);
        loyaltyPointsEarned = 1;
        orderConfirmed = new Order(customer, products, loyaltyPointsEarned, totalPrice, "ORDER_CONFIRMED");
        shoppingCart = new ShoppingCart(customer, products, loyaltyPointsEarned, totalPrice, "OPEN");
    }

    @Test
    public void showConfirmation() {
        Optional<Order> order = orderService.showConfirmation(shoppingCart);
        assertThat(order.get().getCartState(), is("ORDER_CONFIRMED"));
    }

    @Test
    public void placeOrder() {
        Optional<Order> order = orderService.placeOrder(orderConfirmed);
        assertThat(order.get().getCartState(), is("ORDER_PLACED"));
    }

    @Test
    public void placeOrderOverThousandDollars() {
        orderConfirmed = new Order(customer, products, loyaltyPointsEarned, Double.valueOf(1001), "ORDER_CONFIRMED");
        Optional<Order> order = orderService.placeOrder(orderConfirmed);
        assertThat(order.get().getTotalPrice(), is(900.9));
    }

}
