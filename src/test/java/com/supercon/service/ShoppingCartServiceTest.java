package com.supercon.service;

import com.supercon.model.Product;
import com.supercon.model.ShoppingCart;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartServiceTest {

    private String customer;
    private Product product;

    private List<Product> productList;


    @InjectMocks
    private ShoppingCartService shoppingCartService;


    @Before
    public void setup(){
        customer = "Test";
        productList = new ArrayList<>();
        product =  new Product(24.99,"DIS_10-CHAIR_BLUE", "Blue plastic chair");
        productList.add(product);
        shoppingCartService.addProduct(product);
    }

    @After
    public void after(){
        shoppingCartService.removeProduct(new Product(24.99,"DIS_15-CHAIR_BLUE", "Blue plastic chair"));
        shoppingCartService.removeProduct(new Product(24.99,"CHAIR_BLUE", "Blue plastic chair"));
    }

    @Test
    public void shouldCheckoutWithLocaltyPointsEarned() {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.checkout(customer);
        assertThat(shoppingCart.get().getLoyaltyPointsEarned(), is(2));
    }

    @Test
    public void shouldCheckoutWithTotalPriceEarned() {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.checkout(customer);
        assertThat(shoppingCart.get().getTotalPrice(), is(22.4910));
    }

    @Test
    public void shouldAddProduct() {
        Optional<ShoppingCart> shoppingCart = shoppingCartService.checkout(customer);
        assertThat(shoppingCart.get().getProducts().size(), is(1));
    }

    @Test
    public void shouldAddProductWithFifteenPercentDiscount() {
        shoppingCartService.addProduct(new Product(24.99,"DIS_15-CHAIR_BLUE", "Blue plastic chair"));
        Optional<ShoppingCart> shoppingCart = shoppingCartService.checkout(customer);
        assertThat(shoppingCart.get().getLoyaltyPointsEarned(), is(3));
    }

    @Test
    public void shouldAddProductWithFivePercentDiscount() {
        shoppingCartService.addProduct(new Product(24.99,"CHAIR_BLUE", "Blue plastic chair"));
        Optional<ShoppingCart> shoppingCart = shoppingCartService.checkout(customer);
        assertThat(shoppingCart.get().getLoyaltyPointsEarned(), is(6));
    }

    @Test
    public void shouldGetTotalOfProducts(){

        shoppingCartService.addProduct(product);
        shoppingCartService.checkout(customer);

        assertThat(shoppingCartService.getTotal(), is(49.98));

    }

    @Test
    public void shouldRemoveProduct() {

        shoppingCartService.removeProduct(product);

        Optional<ShoppingCart> shoppingCart = shoppingCartService.checkout(customer);
        assertThat(shoppingCart, is(Optional.empty()));

    }

}
