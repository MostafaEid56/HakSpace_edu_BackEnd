package com.hakspace.repository;

import com.hakspace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("""
            UPDATE Product p
            SET p.stockQuantity = p.stockQuantity - 1
            WHERE p.id = :id
              AND p.stockQuantity > 0
            """)
    int decrementStock(@Param("id") Long id);
}
