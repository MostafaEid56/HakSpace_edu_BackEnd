package com.hakspace.repository;

import com.hakspace.model.StoreLead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreLeadRepository extends JpaRepository<StoreLead, Long> {
    Page<StoreLead> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
