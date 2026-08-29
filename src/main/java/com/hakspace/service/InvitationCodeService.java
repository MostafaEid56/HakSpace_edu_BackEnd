package com.hakspace.service;

import com.hakspace.model.InvitationCode;
import com.hakspace.repository.InvitationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InvitationCodeService {

    private final InvitationCodeRepository invitationCodeRepository;
    private static final String CHARACTERS = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public InvitationCode getActiveCode() {
        return invitationCodeRepository.findByIsActiveTrue()
                .orElseGet(this::generateNewCode);
    }

    @Transactional
    public InvitationCode generateNewCode() {
        invitationCodeRepository.deactivateAllCodes();

        String randomStr = generateRandomString(6);
        String codeValue = "HAK-" + randomStr;

        InvitationCode newCode = new InvitationCode();
        newCode.setCode(codeValue);
        newCode.setIsActive(true);

        return invitationCodeRepository.save(newCode);
    }

    public void validateCode(String code) {
        if (code == null || code.isBlank()) {
            throw new RuntimeException("invitation_code.required");
        }

        Optional<InvitationCode> validCode = invitationCodeRepository.findByCodeIgnoreCaseAndIsActiveTrue(code.trim());
        if (validCode.isEmpty()) {
            throw new RuntimeException("invitation_code.invalid");
        }
    }

    private String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
