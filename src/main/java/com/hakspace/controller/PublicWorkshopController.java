package com.hakspace.controller;

import com.hakspace.dto.WorkshopDetailResponse;
import com.hakspace.dto.WorkshopRegistrationRequest;
import com.hakspace.service.WorkshopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshops")
@RequiredArgsConstructor
public class PublicWorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public ResponseEntity<List<WorkshopDetailResponse>> getAll() {
        return ResponseEntity.ok(workshopService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopDetailResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workshopService.getById(id));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            Authentication auth,
            @Valid @RequestBody WorkshopRegistrationRequest req) {
        String login = (auth != null && auth.getName() != null) ? auth.getName() : null;
        return ResponseEntity.ok(workshopService.register(req, login));
    }
}
