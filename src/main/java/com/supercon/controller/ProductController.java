package com.supercon.controller;

import com.supercon.exception.ResourceNotFoundException;
import com.supercon.model.Product;
import com.supercon.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "Product REST Endpoint", description = "Shows the product info")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation(value = "View a list of available products", response = List.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list")})
    @GetMapping("/v1/products")
    public List<String> getProducts() {
        return productService.getProductCodes();
    }

    @ApiOperation(value = "Get a product by product code")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved product"),
            @ApiResponse(code = 404, message = "The product you were trying to reach is not found") })
    @GetMapping("/v1/products/{code}")
    public ResponseEntity<Product> getProduct(@PathVariable final String code) throws ResourceNotFoundException {
        Product product = productService.getProduct(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found for code :: " + code));
        return ResponseEntity.ok().body(product);
    }

}
