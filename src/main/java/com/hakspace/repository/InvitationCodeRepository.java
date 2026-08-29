package com.hakspace.repository;

import com.hakspace.model.InvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationCodeRepository extends JpaRepository<InvitationCode, Long> {
    Optional<InvitationCode> findByIsActiveTrue();
    Optional<InvitationCode> findByCodeIgnoreCaseAndIsActiveTrue(String code);

    @Modifying
    @Query("UPDATE InvitationCode c SET c.isActive = false WHERE c.isActive = true")
    void deactivateAllCodes();
}
