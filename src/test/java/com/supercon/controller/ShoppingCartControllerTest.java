package com.supercon.controller;

import com.supercon.model.Customer;
import com.supercon.model.Order;
import com.supercon.model.Product;
import com.supercon.model.ShoppingCart;
import com.supercon.service.OrderService;
import com.supercon.service.ProductService;
import com.supercon.service.ShoppingCartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.supercon.testdata.TestDataFactory.CHAIR_RED;
import static com.supercon.testdata.TestDataFactory.CHAIR_RED_JSON;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerTest {

    private Customer customer;
    private List<Product> productList;
    private int loyaltyPointsEarned;
    private double totalPrice;
    private ShoppingCart shoppingCart;
    private Order order;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @Before
    public void setup(){
        customer = new Customer("Test");
        productList = new ArrayList<>();
        productList.add(CHAIR_RED);
        loyaltyPointsEarned = 0;
        totalPrice = Double.valueOf(0);
    }

    @Test
    public void shouldAddProductInShoppingCart() throws Exception {
        when(productService.getProduct("CHAIR_RED")).thenReturn(Optional.of(CHAIR_RED));
        mvc.perform(post("/v1/shoppingCart/addProduct/CHAIR_RED"))
                .andExpect(status().isOk())
                .andExpect(content().json(CHAIR_RED_JSON));
    }

    @Test
    public void shouldThrowResourceNotFoundExceptionWithProductIsNotFound() throws Exception {
        when(productService.getProduct("CHAIR_BLACK")).thenReturn(Optional.empty());
        mvc.perform(post("/v1/shoppingCart/addProduct/CHAIR_BLACK"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCheckout() throws Exception {
        shoppingCart = new ShoppingCart(customer, productList, loyaltyPointsEarned, totalPrice,"OPEN");
        when(shoppingCartService.checkout("Test")).thenReturn(Optional.of(shoppingCart));
        mvc.perform(post("/v1/shoppingCart/checkout/Test"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotCheckoutWhenShoppingCartIsEmpty() throws Exception {
        when(shoppingCartService.checkout("Test")).thenReturn(Optional.empty());
        mvc.perform(post("/v1/shoppingCart/checkout/Test"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldConfirmOrder() throws Exception {
        checkoutOrder();
        when(orderService.showConfirmation(shoppingCart)).thenReturn(Optional.of(order));
        mvc.perform(post("/v1/shoppingCart/checkout/Test"))
                .andExpect(status().isOk());
        mvc.perform(post("/v1/shoppingCart/confirmOrder"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotConfirmOrdeWhenShoppingCartIsEmpty() throws Exception {
        shoppingCart = new ShoppingCart(customer, productList, loyaltyPointsEarned, totalPrice,"OPEN");
        order = new Order(customer, productList, loyaltyPointsEarned, totalPrice, "ORDER_CONFIRMED");
        when(orderService.showConfirmation(shoppingCart)).thenReturn(Optional.empty());
        mvc.perform(post("/v1/shoppingCart/confirmOrder"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldPlaceOrder() throws Exception {
        checkoutOrder();
        Order orderPlaced = new Order(customer, productList, loyaltyPointsEarned, totalPrice, "ORDER_PLACED");
        when(orderService.showConfirmation(shoppingCart)).thenReturn(Optional.of(order));
        when(orderService.placeOrder(order)).thenReturn(Optional.of(orderPlaced));
        mvc.perform(post("/v1/shoppingCart/checkout/Test"))
                .andExpect(status().isOk());
        mvc.perform(post("/v1/shoppingCart/confirmOrder"))
                .andExpect(status().isOk());
        mvc.perform(post("/v1/shoppingCart/placeOrder"))
                .andExpect(status().isOk());
    }

    private void checkoutOrder() {
        shoppingCart = new ShoppingCart(customer, productList, loyaltyPointsEarned, totalPrice, "OPEN");
        order = new Order(customer, productList, loyaltyPointsEarned, totalPrice, "ORDER_CONFIRMED");
        when(shoppingCartService.checkout("Test")).thenReturn(Optional.of(shoppingCart));
    }

    @Test
    public void shouldNotPlaceOrdeWhenShoppingCartIsEmpty() throws Exception {
        shoppingCart = new ShoppingCart(customer, productList, loyaltyPointsEarned, totalPrice, "OPEN");
        order = new Order(customer, productList, loyaltyPointsEarned, totalPrice, "ORDER_CONFIRMED");
        when(orderService.placeOrder(order)).thenReturn(Optional.empty());
        mvc.perform(post("/v1/shoppingCart/placeOrder"))
                .andExpect(status().isNotFound());
    }

}
