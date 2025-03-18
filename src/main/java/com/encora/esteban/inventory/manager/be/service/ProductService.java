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
        productList.add(new Product(1L, "Sandwich", "Food", 999.99, LocalDate.of(2025, 4, 30), 1, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(2L, "Mouse", "Electronics", 25.99, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(3L, "Socks", "Clothing", 3.50, null, 3, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(4L, "Keyboard", "Electronics", 49.99, null, 4, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(5L, "Bread", "Food", 5.99, LocalDate.of(2025, 8, 20), 5, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(6L, "Socks", "Clothing", 3.50, null, 6, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(7L, "Keyboard", "Electronics", 49.99, null, 7, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(8L, "Bread", "Food", 5.99, LocalDate.of(2025, 8, 20), 8, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(9L, "Socks", "Clothing", 3.50, null, 9, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(10L, "Bread", "Food", 5.99, LocalDate.of(2025, 8, 20), 10, LocalDate.now(), LocalDate.now()));
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
        for (int i = 0; i < productList.size(); i++) {
            Product p = productList.get(i);
            if (p.getId().equals(id)) {
                p.setName(updatedProduct.getName());
                p.setCategory(updatedProduct.getCategory());
                p.setUnitPrice(updatedProduct.getUnitPrice());
                p.setQuantityInStock(updatedProduct.getQuantityInStock());
                p.setUpdateDate(LocalDate.now());

                // ✅ Ensure expiration date is updated if provided
                if (updatedProduct.getExpirationDate() != null) {
                    p.setExpirationDate(updatedProduct.getExpirationDate());
                    System.out.println("✅ Expiration Date Updated: " + p.getExpirationDate()); // Debug log
                } else {
                    System.out.println("⚠️ No Expiration Date Provided, Keeping Old Value.");
                }

                productList.set(i, p); // ✅ Save the updated product in memory
                System.out.println("✅ Product updated: " + p.toString());
                return true;
            }
        }
        return false;
    }



    public boolean markProductOutOfStock(Long id) {
        for (int i = 0; i < productList.size(); i++) {
            Product p = productList.get(i);
            if (p.getId().equals(id)) {
                p.setQuantityInStock(0);
                p.setUpdateDate(LocalDate.now());

                productList.set(i, p); // ✅ Ensure productList updates correctly
                System.out.println("✅ Product marked as out of stock: " + p.toString());
                return true;
            }
        }
        return false;
    }

    public boolean restoreProductStock(Long id) {
        for (int i = 0; i < productList.size(); i++) {
            Product p = productList.get(i);
            if (p.getId().equals(id)) {
                p.setQuantityInStock(10);
                p.setUpdateDate(LocalDate.now());

                productList.set(i, p); // ✅ Ensure productList updates correctly
                System.out.println("✅ Product stock restored: " + p.toString());
                return true;
            }
        }
        return false;
    }



}
