package com.encora.esteban.inventory.manager.be.controller;

import com.encora.esteban.inventory.manager.be.model.Product;
import com.encora.esteban.inventory.manager.be.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final ProductService productService;

    public InventoryController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean inStock) {
        return productService.getProducts(name, category, inStock);
    }
}
