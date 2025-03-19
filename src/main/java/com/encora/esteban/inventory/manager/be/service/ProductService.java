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
        productList.add(new Product(1L, "Sandwich", "Food", 10, LocalDate.of(2025, 4, 30), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(2L, "Mouse", "Electronics", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(3L, "Socks", "Clothing", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(4L, "Keyboard", "Electronics", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(5L, "Bread", "Food", 10, LocalDate.of(2025, 8, 20), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(6L, "Socks", "Clothing", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(7L, "Keyboard", "Electronics", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(8L, "Bread", "Food", 10, LocalDate.of(2025, 8, 20), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(9L, "Socks", "Clothing", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(10L, "Bread", "Food", 10, LocalDate.of(2025, 8, 20), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(11L, "Sandwich", "Food", 10, LocalDate.of(2025, 4, 30), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(12L, "Mouse", "Electronics", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(13L, "Socks", "Clothing", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(14L, "Keyboard", "Electronics", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(15L, "Bread", "Food", 10, LocalDate.of(2025, 8, 20), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(16L, "Socks", "Clothing", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(17L, "Keyboard", "Electronics", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(18L, "Bread", "Food", 10, LocalDate.of(2025, 8, 20), 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(19L, "Socks", "Clothing", 10, null, 2, LocalDate.now(), LocalDate.now()));
        productList.add(new Product(20L, "Bread", "Food", 10, LocalDate.of(2025, 8, 20), 2, LocalDate.now(), LocalDate.now()));

    }

    // Fetch all products with optional filtering and pagination
    public Map<String, Object> getProducts(String name, String category, Boolean inStock, int page, int size, String sortBy, String sortOrder) {
        List<Product> filteredProducts = productList.stream()
                .filter(product -> (name == null || product.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(product -> (category == null || product.getCategory().equalsIgnoreCase(category)))
                .filter(product -> (inStock == null || (inStock ? product.getQuantityInStock() > 0 : product.getQuantityInStock() == 0)))
                .collect(Collectors.toList());

        // ✅ Apply Sorting BEFORE Pagination
        Comparator<Product> comparator = Comparator.comparing(Product::getName); // Default sorting by name

        if ("category".equals(sortBy)) {
            comparator = Comparator.comparing(Product::getCategory);
        } else if ("price".equals(sortBy)) {
            comparator = Comparator.comparingDouble(Product::getUnitPrice);
        } else if ("stock".equals(sortBy)) {
            comparator = Comparator.comparingInt(Product::getQuantityInStock);
        } else if ("expiration".equals(sortBy)) {
            comparator = Comparator.comparing(Product::getExpirationDate, Comparator.nullsLast(Comparator.naturalOrder())); // Handle null dates
        }

        if ("desc".equals(sortOrder)) {
            comparator = comparator.reversed();
        }

        filteredProducts.sort(comparator); // ✅ Sorting applied before pagination

        // ✅ Apply Pagination AFTER Sorting
        int totalProducts = filteredProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / size);

        // ✅ ADD NEW CATEGORY-BASED TOTALS (FULL INVENTORY, NOT PAGINATED)
        Map<String, Integer> categoryStock = new HashMap<>();
        Map<String, Double> categoryValue = new HashMap<>();

        int totalStock = 0; // ✅ Initialize totalStock here
        double totalValue = 0.0; // ✅ Initialize totalValue here

        for (Product product : productList) { // ✅ Loop through ALL products, not just paginated ones
            String productCategory = product.getCategory(); // 🔄 Renamed to productCategory
            int stock = product.getQuantityInStock();
            double value = product.getUnitPrice() * stock;

            categoryStock.put(productCategory, categoryStock.getOrDefault(productCategory, 0) + stock);
            categoryValue.put(productCategory, categoryValue.getOrDefault(productCategory, 0.0) + value);

            totalStock += stock;
            totalValue += value;
        }


        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalProducts);
        List<Product> paginatedProducts = (fromIndex >= totalProducts) ? new ArrayList<>() : filteredProducts.subList(fromIndex, toIndex);

        // ✅ Debugging Statements (Print Values to Check)
        System.out.println("DEBUG: Total Products = " + totalProducts);
        System.out.println("DEBUG: Total Pages = " + totalPages);
        System.out.println("DEBUG: Total Stock = " + totalStock);
        System.out.println("DEBUG: Total Value = " + totalValue);
        System.out.println("DEBUG: Paginated Products Count = " + paginatedProducts.size());
        System.out.println("DEBUG: Category Stock Map = " + categoryStock);
        System.out.println("DEBUG: Category Value Map = " + categoryValue);


        // ✅ Return sorted, paginated results
        Map<String, Object> response = new HashMap<>();
        response.put("products", paginatedProducts);
        response.put("totalProducts", totalProducts);
        response.put("totalPages", totalPages);
        response.put("totalStock", Math.max(totalStock, 0)); // ✅ Use FULL inventory totalStock
        response.put("totalValue", Math.max(totalValue, 0)); // ✅ Use FULL inventory totalValue
        response.put("categoryStock", categoryStock); // ✅ Add full category stock
        response.put("categoryValue", categoryValue); // ✅ Add full category value
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
