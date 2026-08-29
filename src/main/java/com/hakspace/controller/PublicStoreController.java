package com.hakspace.controller;

import com.hakspace.model.Product;
import com.hakspace.model.StoreLead;
import com.hakspace.service.StoreLeadService;
import com.hakspace.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class PublicStoreController {

    private final StoreService storeService;
    private final StoreLeadService storeLeadService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(storeService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getProductById(id));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<com.hakspace.model.ProductCategory>> getAllCategories() {
        return ResponseEntity.ok(storeService.getAllCategories());
    }

    @PostMapping("/products/{id}/purchase")
    public ResponseEntity<StoreLead> submitPurchaseRequest(@PathVariable Long id, @RequestBody StoreLead leadReq) {
        return ResponseEntity.ok(storeLeadService.createLead(leadReq, id));
    }
}
