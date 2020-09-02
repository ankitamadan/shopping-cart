package com.supercon.testdata;

import com.supercon.model.Product;

import java.util.Arrays;
import java.util.List;

public class TestDataFactory {

    public static final Product CHAIR_RED = new Product(24.99, "CHAIR_RED", "Red plastic chair");

    public static final String CHAIR_RED_JSON =
            "{\"price\":24.99,\"productCode\":\"CHAIR_RED\",\"name\":\"Red plastic chair\"}";

    public static final List<Product> PRODUCT_LIST = Arrays.asList(
            new Product(24.99, "CHAIR_RED", "Red plastic chair"),
            new Product(24.99, "DIS_10-CHAIR_BLUE", "Blue plastic chair"),
            new Product(24.99, "CHAIR_WHITE", "White plastic chair"),
            new Product(14.99, "STOOL_WHITE", "White plastic footstool"),
            new Product(14.99, "DIS_15-STOOL_GREEN", "Green plastic footstool"),
            new Product(74.99, "COMP_DESK", "Wooden computer desk"),
            new Product(129.99, "COMP_CHAIR", "Computer swivel chair"),
            new Product(249.99, "BOARD_TABLE", "12-person boardroom table"),
            new Product(99.99, "BOARD_CHAIR", "Boardroom chair")
    );
}
