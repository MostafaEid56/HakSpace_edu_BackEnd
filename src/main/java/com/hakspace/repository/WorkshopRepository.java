package com.hakspace.repository;

import com.hakspace.model.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
    List<Workshop> findByStatus(Workshop.WorkshopStatus status);
}
