package com.encora.esteban.inventory.manager.be.controller;

import com.encora.esteban.inventory.manager.be.model.Product;
import com.encora.esteban.inventory.manager.be.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product sampleProduct;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        sampleProduct = new Product(
                1L,
                "Laptop",
                "Electronics",
                500.0,
                LocalDate.of(2025, 12, 31),
                10,
                LocalDate.now(),
                LocalDate.now()
        );

        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void getAllProducts_returnsProductList() throws Exception {
        when(productService.getProducts(
                null,
                null,
                null,
                0,
                10,
                "name",
                "asc"))
                .thenReturn(Map.of("products", List.of(sampleProduct)));

        mockMvc.perform(get("/inventory/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].name").value("Laptop"));
    }

    @Test
    void addProduct_returnsCreatedProduct() throws Exception {
        when(productService.addProduct(any(Product.class))).thenReturn(sampleProduct);

        mockMvc.perform(post("/inventory/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void updateProduct_returnsUpdatedProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(true);

        mockMvc.perform(put("/inventory/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleProduct)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_returnsSuccess() throws Exception {
        when(productService.deleteProductById(1L)).thenReturn(true);

        mockMvc.perform(delete("/inventory/products/1"))
                .andExpect(status().isOk());
    }
}
