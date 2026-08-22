package com.hakspace.repository;

import com.hakspace.model.InstructorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InstructorProfileRepository extends JpaRepository<InstructorProfile, Long> {
    Optional<InstructorProfile> findByUserId(Long userId);
    Optional<InstructorProfile> findByUserUsername(String username);
    boolean existsByUserId(Long userId);
}
