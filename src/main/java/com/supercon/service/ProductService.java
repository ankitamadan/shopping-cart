package com.supercon.service;

import com.supercon.model.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    // Simple source of products: would come from a database in the real world.
    private static final List<Product> ALL_PRODUCTS = Arrays.asList(
            new Product(1000.99, "CHAIR_RED", "Red plastic chair"),
            new Product(24.99, "DIS_10-CHAIR_BLUE", "Blue plastic chair"),
            new Product(24.99, "CHAIR_WHITE", "White plastic chair"),
            new Product(14.99, "STOOL_WHITE", "White plastic footstool"),
            new Product(14.99, "DIS_15-STOOL_GREEN", "Green plastic footstool"),
            new Product(74.99, "COMP_DESK", "Wooden computer desk"),
            new Product(129.99, "COMP_CHAIR", "Computer swivel chair"),
            new Product(249.99, "BOARD_TABLE", "12-person boardroom table"),
            new Product(99.99, "BOARD_CHAIR", "Boardroom chair")
    );

    private List<Product> products;

    public ProductService() {
        this.products = ALL_PRODUCTS;
    }

    public ProductService(final List<Product> products) {
        this.products = products;
    }

    public List<String> getProductCodes() {
        return products.stream()
                .map(Product::getProductCode)
                .collect(Collectors.toList());
    }

    public Optional<Product> getProduct(final String code) {
        return products.stream()
                .filter(product -> product.getProductCode().equals(code)).findFirst();
       /* for (final Product product : products) {
            if (product.getProductCode().equals(code)) {
                return product;
            }
        }
        return null;*/
    }
}
