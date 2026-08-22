package com.hakspace.repository;

import com.hakspace.model.WorkshopRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WorkshopRegistrationRepository extends JpaRepository<WorkshopRegistration, Long> {
    List<WorkshopRegistration> findByWorkshopId(Long workshopId);
    List<WorkshopRegistration> findByUserId(Long userId);
    List<WorkshopRegistration> findByEmail(String email);
    boolean existsByWorkshopIdAndEmail(Long workshopId, String email);
    boolean existsByWorkshopIdAndUserId(Long workshopId, Long userId);
}
