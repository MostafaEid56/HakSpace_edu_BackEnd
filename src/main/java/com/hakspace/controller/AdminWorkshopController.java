package com.hakspace.controller;

import com.hakspace.dto.WorkshopDetailResponse;
import com.hakspace.dto.WorkshopRequest;
import com.hakspace.service.WorkshopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/workshops")
@RequiredArgsConstructor
public class AdminWorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public ResponseEntity<List<WorkshopDetailResponse>> getAll() {
        return ResponseEntity.ok(workshopService.adminGetAll());
    }

    @PostMapping
    public ResponseEntity<WorkshopDetailResponse> create(@Valid @RequestBody WorkshopRequest req) {
        return ResponseEntity.ok(workshopService.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkshopDetailResponse> update(@PathVariable Long id,
                                                         @Valid @RequestBody WorkshopRequest req) {
        return ResponseEntity.ok(workshopService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        workshopService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WorkshopDetailResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.isBlank()) {
            throw new RuntimeException("workshop.status.required");
        }
        return ResponseEntity.ok(workshopService.updateStatus(id, status));
    }

    @GetMapping("/{id}/registrations")
    public ResponseEntity<?> getRegistrations(@PathVariable Long id) {
        return ResponseEntity.ok(workshopService.getRegistrations(id));
    }

    @PatchMapping("/registrations/{regId}/status")
    public ResponseEntity<?> updateRegistrationStatus(
            @PathVariable Long regId,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        return ResponseEntity.ok(workshopService.updateRegistrationStatus(regId, status));
    }

    @DeleteMapping("/registrations/{regId}")
    public ResponseEntity<?> deleteRegistration(@PathVariable Long regId) {
        workshopService.deleteRegistration(regId);
        return ResponseEntity.ok().build();
    }
}
