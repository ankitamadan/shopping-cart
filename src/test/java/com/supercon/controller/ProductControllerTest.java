package com.supercon.controller;

import com.supercon.model.Product;
import com.supercon.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private static final Product CHAIR_RED = new Product(24.99, "CHAIR_RED", "Red plastic chair");
    private static final String CHAIR_RED_JSON =
            "{\"price\":24.99,\"productCode\":\"CHAIR_RED\",\"name\":\"Red plastic chair\"}";

    private static final List<String> PRODUCT_CODES =
            Arrays.asList("CHAIR_RED", "DIS_10-CHAIR_BLUE", "CHAIR_WHITE");
    private static final String PRODUCT_CODES_JSON = "[\"CHAIR_RED\",\"DIS_10-CHAIR_BLUE\",\"CHAIR_WHITE\"]";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Test
    public void getProduct_should_return_existing_product() throws Exception {
        when(productService.getProduct("CHAIR_RED")).thenReturn(Optional.of(CHAIR_RED));
        mvc.perform(get("/v1/products/CHAIR_RED"))
                .andExpect(status().isOk())
                .andExpect(content().json(CHAIR_RED_JSON));
    }

    @Test
    public void getProduct_should_return_404_for_missing_product() throws Exception {
        when(productService.getProduct("CHAIR_BLACK")).thenReturn(Optional.empty());
        mvc.perform(get("/v1/products/CHAIR_BLACK"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProducts_should_return_the_current_list_of_products() throws Exception {
        when(productService.getProductCodes()).thenReturn(PRODUCT_CODES);
        mvc.perform(get("/v1/products"))
                .andExpect(status().isOk())
                .andExpect(content().json(PRODUCT_CODES_JSON));
    }
}
