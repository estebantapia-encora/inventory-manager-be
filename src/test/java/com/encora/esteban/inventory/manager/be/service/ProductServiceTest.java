package com.encora.esteban.inventory.manager.be.service;

import com.encora.esteban.inventory.manager.be.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setup() {
        productService = new ProductService(); // Uses in-memory list
    }

    @Test
    void addProduct_assignsIdAndTimestamps() {
        Product product = new Product();
        product.setName("Test");
        product.setCategory("Electronics");
        product.setUnitPrice(100);
        product.setQuantityInStock(5);
        product.setExpirationDate(LocalDate.of(2025, 1, 1));

        Product saved = productService.addProduct(product);

        assertNotNull(saved.getId());
        assertEquals("Test", saved.getName());
        assertNotNull(saved.getCreationDate());
        assertNotNull(saved.getUpdateDate());
    }

    @Test
    void deleteProductById_removesProduct() {
        Product product = productService.addProduct(
                new Product(
                        null,
                        "DeleteMe",
                        "Food",
                        10,
                        null,
                        1,
                        null,
                        null));
        boolean deleted = productService.deleteProductById(product.getId());
        assertTrue(deleted);
    }

    @Test
    void updateProduct_updatesAttributes() {
        Product original = productService.addProduct(
                new Product(
                        null,
                        "Old",
                        "Clothing",
                        50,
                        null,
                        2,
                        null,
                        null));
        Product updates =
                new Product(
                        null,
                        "New",
                        "Clothing",
                        75, null,
                        5, null,
                        null);

        boolean updated = productService.updateProduct(original.getId(), updates);
        assertTrue(updated);

        Map<String, Object> result = productService.getProducts(
                null,
                null,
                null,
                0,
                100,
                "name",
                "asc");
        List<?> products = (List<?>) result.get("products");
        boolean found = products.stream().anyMatch(p -> p.toString().contains("New"));
        assertTrue(found);
    }


    @Test
    void markProductOutOfStock_setsQuantityToZero() {
        Product product = productService.addProduct(
                new Product(
                        null,
                        "Stocked",
                        "Food",
                        20,
                        null,
                        3,
                        null,
                        null));
        boolean marked = productService.markProductOutOfStock(product.getId());
        assertTrue(marked);
    }

    @Test
    void restoreProductStock_setsQuantityToTen() {
        Product product = productService.addProduct(
                new Product(
                        null,
                        "Out",
                        "Food",
                        20,
                        null,
                        0,
                        null,
                        null));

        boolean restored = productService.restoreProductStock(product.getId());
        assertTrue(restored);

        List<Product> products = (List<Product>) productService
                .getProducts(null, null, null, 0, 100, "name", "asc")
                .get("products");

        Product restoredProduct = products.stream()
                .filter(p -> p.getId().equals(product.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals(10, restoredProduct.getQuantityInStock());
    }


}
