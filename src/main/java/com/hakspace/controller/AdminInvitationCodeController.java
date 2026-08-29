package com.hakspace.controller;

import com.hakspace.model.InvitationCode;
import com.hakspace.service.InvitationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/invitation-code")
@RequiredArgsConstructor
public class AdminInvitationCodeController {

    private final InvitationCodeService invitationCodeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getActiveInvitationCode() {
        InvitationCode activeCode = invitationCodeService.getActiveCode();
        return ResponseEntity.ok(Map.of(
                "id", activeCode.getId(),
                "code", activeCode.getCode(),
                "status", activeCode.getIsActive() ? "ACTIVE" : "EXPIRED",
                "createdAt", activeCode.getCreatedAt()
        ));
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateNewInvitationCode() {
        InvitationCode newCode = invitationCodeService.generateNewCode();
        return ResponseEntity.ok(Map.of(
                "id", newCode.getId(),
                "code", newCode.getCode(),
                "status", newCode.getIsActive() ? "ACTIVE" : "EXPIRED",
                "createdAt", newCode.getCreatedAt()
        ));
    }
}
