package com.hakspace.controller;

import com.hakspace.dto.InstructorProfileResponse;
import com.hakspace.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class PublicInstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public ResponseEntity<List<InstructorProfileResponse>> getAll() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    @GetMapping("/{username}")
    public ResponseEntity<InstructorProfileResponse> getByUsername(@PathVariable String username) {
        return ResponseEntity.ok(instructorService.getByUsername(username));
    }
}
