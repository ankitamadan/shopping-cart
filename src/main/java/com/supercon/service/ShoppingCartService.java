package com.supercon.service;

import com.supercon.model.Customer;
import com.supercon.model.Product;
import com.supercon.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    public static final String DISCOUNT_TEN = "10";
    public static final String DISCOUNT_FIFTEEN = "15";

    private List<Product> products = new ArrayList<>();

    private final OrderService orderService;

    public ShoppingCartService(OrderService orderService){
        this.orderService = orderService;
    }
    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }


    /*
        Checkout: Calculates total price and total loyalty points earned by the customer.
        Products with product code starting with DIS_10 have a 10% discount applied.
        Products with product code starting with DIS_15 have a 15% discount applied.

        Loyalty points are earned more when the product is not under any offer.
            Customer earns 1 point on every $5 purchase.
            Customer earns 1 point on every $10 spent on a product with 10% discount.
            Customer earns 1 point on every $15 spent on a product with 15% discount.
    */

    public Optional<ShoppingCart> checkout(String customerName) {
        Customer customer = new Customer(customerName);
        double totalPrice = 0;

        int loyaltyPointsEarned = 0;
        Optional<ShoppingCart> shoppingCart = Optional.empty();
        for (Product product : products) {
            double discount = 0;
            String discountString = product.getProductCode().substring(4,6);
            switch (discountString) {
                case DISCOUNT_TEN :
                    discount = (product.getPrice() * 0.1);
                    loyaltyPointsEarned += (product.getPrice() / 10);
                    break;
                case DISCOUNT_FIFTEEN :
                    discount = (product.getPrice() * 0.15);
                    loyaltyPointsEarned += (product.getPrice() / 15);
                    break;
                 default:
                     loyaltyPointsEarned += (product.getPrice() / 5);
            }

            totalPrice += product.getPrice() - discount;
        }

        return products.size() > 0 ?
                Optional.of(new ShoppingCart(customer, products, loyaltyPointsEarned, totalPrice,"OPEN")) : shoppingCart;
    }

    public Double getTotal() {
        return products.stream()
                .map(product -> product.getPrice())
                .reduce(Double::sum)
                .orElse(0.0);
    }

}
