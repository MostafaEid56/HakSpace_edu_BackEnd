package com.hakspace.controller;

import com.hakspace.model.Product;
import com.hakspace.model.ProductCategory;
import com.hakspace.model.StoreLead;
import com.hakspace.service.StoreLeadService;
import com.hakspace.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/store")
@RequiredArgsConstructor
public class AdminStoreController {

    private final StoreService storeService;
    private final StoreLeadService storeLeadService;

    // Categories
    @GetMapping("/categories")
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        return ResponseEntity.ok(storeService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<ProductCategory> createCategory(@RequestBody ProductCategory category) {
        return ResponseEntity.ok(storeService.createCategory(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Long id, @RequestBody ProductCategory category) {
        return ResponseEntity.ok(storeService.updateCategory(id, category));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        storeService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    // Products
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(storeService.getAllProducts());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return ResponseEntity.ok(storeService.createProduct(product));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(storeService.updateProduct(id, product));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        storeService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/products/upload-image")
    public ResponseEntity<Map<String, String>> uploadProductImage(
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        String imageUrl = storeService.uploadProductImage(file);
        return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
    }

    // Leads
    @GetMapping("/leads")
    public ResponseEntity<Page<StoreLead>> getLeads(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size) {
        return ResponseEntity.ok(storeLeadService.getLeads(page, size));
    }

    @PatchMapping("/leads/{id}/status")
    public ResponseEntity<StoreLead> updateLeadStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        StoreLead.StoreLeadStatus status = StoreLead.StoreLeadStatus.valueOf(statusStr);
        return ResponseEntity.ok(storeLeadService.updateLeadStatus(id, status));
    }
}
