package com.supercon.controller;

import com.supercon.model.Order;
import com.supercon.model.Product;
import com.supercon.model.ShoppingCart;
import com.supercon.service.OrderService;
import com.supercon.service.ProductService;
import com.supercon.service.ShoppingCartService;
import com.supercon.exception.EmptyShoppingCartException;
import com.supercon.exception.ResourceNotFoundException;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Product REST Endpoint", description = "Managing Shopping Cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;
    private final OrderService orderService;
    private ShoppingCart shoppingCart;
    private Order confirmedOrder;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService, OrderService orderService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @ApiOperation(value = "Add a product")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Product Successfully added"),
            @ApiResponse(code = 404, message = "The product you were trying to reach is not found") })
    @PostMapping("/v1/shoppingCart/addProduct/{productId}")
    public ResponseEntity<Product> addProductToCart(
            @ApiParam(value = "Product code from which product object will retrieve", required = true)
            @PathVariable(value = "productId") String productId) throws ResourceNotFoundException {
        Product product = productService.getProduct(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for productId :: " + productId));
        shoppingCartService.addProduct(product);
        return ResponseEntity.ok().body(product);
    }

    @ApiOperation(value = "Checkout")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Shopping cart successfully checked out"),
            @ApiResponse(code = 404, message = "Shopping cart is empty") })
    @PostMapping("/v1/shoppingCart/checkout/{customerName}")
    public ResponseEntity<ShoppingCart> checkout(
            @ApiParam(value = "Customer name", required = true)
            @PathVariable(value = "customerName") String customerName) throws EmptyShoppingCartException {
        shoppingCart = shoppingCartService.checkout(customerName)
                .orElseThrow(() -> new EmptyShoppingCartException("Shopping cart is empty"));
        return ResponseEntity.ok().body(shoppingCart);
    }

    @ApiOperation(value = "Confirm order")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Order confirmed")})
    @PostMapping("/v1/shoppingCart/confirmOrder")
    public ResponseEntity<ShoppingCart> confirmOrder() throws EmptyShoppingCartException {
        Order order = orderService.showConfirmation(shoppingCart)
                .orElseThrow(() -> new EmptyShoppingCartException("Shopping cart is empty"));
        this.confirmedOrder = order;
        return ResponseEntity.ok().body(order);
    }

    @ApiOperation(value = "Place order")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Order checked out")})
    @PostMapping("/v1/shoppingCart/placeOrder")
    public ResponseEntity<ShoppingCart> placeOrder() throws EmptyShoppingCartException {
        Order order = orderService.placeOrder(this.confirmedOrder)
                .orElseThrow(() -> new EmptyShoppingCartException("Shopping cart is empty"));
        return ResponseEntity.ok().body(order);
    }


}
