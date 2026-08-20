package com.hakspace.service;

import com.hakspace.dto.CertificateBlockedResponse;
import com.hakspace.dto.CertificateResponse;
import com.hakspace.model.Blacklist;
import com.hakspace.model.Certificate;
import com.hakspace.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepo;
    private final BlacklistService blacklistService;

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Object verifyCertificate(String certificateId) {
        Certificate cert = certificateRepo.findByCertificateId(certificateId)
                .orElseThrow(() -> new RuntimeException("certificate.not.found"));

        // Note: The student's phone number is stored in the 'email' field to ensure uniqueness
        String studentPhone = cert.getStudent().getEmail();
        Optional<Blacklist> blacklisted = blacklistService.findActiveByPhone(studentPhone);

        // Also check by full name to support name-only blacklist entries
        if (blacklisted.isEmpty()) {
            blacklisted = blacklistService.findActiveByName(cert.getStudentName());
        }

        if (blacklisted.isPresent()) {
            return new CertificateBlockedResponse(true, blacklisted.get().getReason());
        }

        return CertificateResponse.from(cert);
    }
}
