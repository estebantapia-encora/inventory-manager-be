package com.encora.esteban.inventory.manager.be.service;

import com.encora.esteban.inventory.manager.be.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final List<Product> productList = new ArrayList<>();

    public ProductService() {
        // Preload some dummy data
        productList.add(new Product(1L, "Laptop", "Electronics", 999.99, LocalDate.now().plusDays(30), 10, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(2L, "Mouse", "Electronics", 25.99, LocalDate.now().plusDays(15), 5, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(3L, "Notebook", "Stationery", 3.50, null, 20, LocalDate.now(), LocalDate.now()));
    }

    // Fetch all products with optional filtering
    public List<Product> getProducts(String name, String category, Boolean inStock) {
        return productList.stream()
                .filter(product -> (name == null || product.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(product -> (category == null || product.getCategory().equalsIgnoreCase(category)))
                .filter(product -> (inStock == null || (inStock ? product.getQuantityInStock() > 0 : product.getQuantityInStock() == 0)))
                .collect(Collectors.toList());
    }
}
