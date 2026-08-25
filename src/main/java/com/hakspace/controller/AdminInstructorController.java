package com.hakspace.controller;

import com.hakspace.dto.InstructorProfileRequest;
import com.hakspace.dto.InstructorProfileResponse;
import com.hakspace.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/instructors")
@RequiredArgsConstructor
public class AdminInstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public ResponseEntity<List<InstructorProfileResponse>> getAll() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @PostMapping("/{userId}/promote")
    public ResponseEntity<InstructorProfileResponse> promote(
            @PathVariable Long userId,
            @RequestBody(required = false) InstructorProfileRequest req) {
        return ResponseEntity.ok(instructorService.promoteToInstructor(userId, req != null ? req : new InstructorProfileRequest()));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<InstructorProfileResponse> update(
            @PathVariable Long userId,
            @RequestBody InstructorProfileRequest req) {
        return ResponseEntity.ok(instructorService.updateProfile(userId, req));
    }

    @DeleteMapping("/{userId}/demote")
    public ResponseEntity<?> demote(@PathVariable Long userId) {
        instructorService.demoteInstructor(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<InstructorProfileResponse> uploadProfileImage(
            @PathVariable Long userId,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        return ResponseEntity.ok(instructorService.uploadProfileImage(userId, file));
    }
}
