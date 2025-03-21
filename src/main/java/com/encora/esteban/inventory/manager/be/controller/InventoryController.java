package com.encora.esteban.inventory.manager.be.controller;
import com.encora.esteban.inventory.manager.be.model.Product;

import com.encora.esteban.inventory.manager.be.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;


import java.util.Map;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductService productService;

    public InventoryController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public Map<String, Object> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productService.getProducts(name, category, inStock, page, size);
    }

    // ✅ ADD THIS DELETE MAPPING
    @DeleteMapping("/products/{id}")

    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProductById(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build(); // 404 if product doesn't exist
        }
    }

    // ✅ ADD THIS EDIT MAPPING
    @PutMapping("/products/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable Long id,
            @RequestBody Product updatedProduct) {
        boolean updated = productService.updateProduct(id, updatedProduct);

        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build(); // 404 if product doesn't exist
        }
    }


}
