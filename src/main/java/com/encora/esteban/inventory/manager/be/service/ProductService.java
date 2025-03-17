package com.encora.esteban.inventory.manager.be.service;

import com.encora.esteban.inventory.manager.be.model.Product;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final List<Product> productList = new ArrayList<>();

    public ProductService() {
        // Preload some dummy data
        productList.add(new Product(1L, "Sandwich", "Food", 999.99, LocalDate.of(2025, 4, 30), 10, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(2L, "Mouse", "Electronics", 25.99, null, 5, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(3L, "Notebook", "Clothing", 3.50, null, 20, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(4L, "Keyboard", "Electronics", 49.99, null, 15, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(14L, "Bread", "Food", 5.99, LocalDate.of(2025, 8, 20), 30, LocalDate.now(), LocalDate.now()));
    }

    // Fetch all products with optional filtering and pagination
    public Map<String, Object> getProducts(String name, String category, Boolean inStock, int page, int size) {
        List<Product> filteredProducts = productList.stream()
                .filter(product -> (name == null || product.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(product -> (category == null || product.getCategory().equalsIgnoreCase(category)))
                .filter(product -> (inStock == null || (inStock ? product.getQuantityInStock() > 0 : product.getQuantityInStock() == 0)))
                .collect(Collectors.toList());

        // Get the total number of products after filtering
        int totalProducts = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        // Apply pagination
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalProducts);

        List<Product> paginatedProducts = fromIndex >= totalProducts ? new ArrayList<>() : filteredProducts.subList(fromIndex, toIndex);

        // Create a response map
        Map<String, Object> response = new HashMap<>();
        response.put("products", paginatedProducts);
        response.put("totalProducts", totalProducts);
        response.put("totalPages", totalPages);
        response.put("currentPage", page);

        return response;
    }

    // Add this method inside `ProductService`
    public boolean deleteProductById(Long id) {
        System.out.println("🗑️ Received delete request for ID: " + id);

        Optional<Product> productToDelete = productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();

        if (productToDelete.isPresent()) {
            System.out.println("🗑️ Deleting product: " + productToDelete.get().toString());

            productList.remove(productToDelete.get()); // Remove from the list

            System.out.println("✅ Product with ID " + id + " deleted successfully.");
            return true; // ✅ Product deleted
        }

        System.out.println("❌ Product with ID " + id + " not found.");
        return false; // ❌ Product not found
    }


    public boolean updateProduct(Long id, Product updatedProduct) {
        System.out.println("🔄 Received update request for ID: " + id);
        System.out.println("📥 Incoming product data: " + updatedProduct.toString()); // ✅ Debugging log

        Optional<Product> existingProduct = productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            System.out.println("🔄 Before update: " + product.toString());

            // ✅ Ensure field mappings are correct
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setUnitPrice(updatedProduct.getUnitPrice());
            product.setExpirationDate(updatedProduct.getExpirationDate());
            product.setQuantityInStock(updatedProduct.getQuantityInStock());
            product.setUpdateDate(LocalDate.now());

            System.out.println("✅ After update: " + product.toString());

            return true; // ✅ Product updated
        }

        System.out.println("❌ Product with ID " + id + " not found.");
        return false; // ❌ Product not found
    }


}
