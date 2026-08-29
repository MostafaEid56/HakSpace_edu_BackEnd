package com.hakspace.service;

import com.hakspace.model.Product;
import com.hakspace.model.ProductCategory;
import com.hakspace.repository.ProductCategoryRepository;
import com.hakspace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ImageStorageService imageStorageService;

    // Categories
    public List<ProductCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public ProductCategory createCategory(ProductCategory category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public ProductCategory updateCategory(Long id, ProductCategory categoryReq) {
        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryReq.getName());
        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product createProduct(Product productReq) {
        if (productReq.getCategory() != null && productReq.getCategory().getId() != null) {
            ProductCategory category = categoryRepository.findById(productReq.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            productReq.setCategory(category);
        }
        return productRepository.save(productReq);
    }

    @Transactional
    public Product updateProduct(Long id, Product productReq) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setName(productReq.getName());
        product.setDescription(productReq.getDescription());
        product.setPrice(productReq.getPrice());
        product.setStockQuantity(productReq.getStockQuantity());
        product.setImageUrl(productReq.getImageUrl());
        
        if (productReq.getCategory() != null && productReq.getCategory().getId() != null) {
            ProductCategory category = categoryRepository.findById(productReq.getCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }
        
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public String uploadProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("image.empty");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("image.too_large");
        }
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/webp"))) {
            throw new RuntimeException("image.invalid_format");
        }
        return imageStorageService.upload(file);
    }
}
