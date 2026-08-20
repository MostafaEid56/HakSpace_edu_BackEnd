package com.hakspace.controller;

import com.hakspace.dto.RegisterRequest;
import com.hakspace.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.registerCommunityMember(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> creds) {
        String loginIdentifier = creds.get("login");
        if (loginIdentifier == null || loginIdentifier.isBlank()) {
            loginIdentifier = creds.get("email");
        }
        if (loginIdentifier == null || loginIdentifier.isBlank()) {
            loginIdentifier = creds.get("username");
        }
        String password = creds.get("password");
        if (loginIdentifier == null || password == null) {
            throw new RuntimeException("auth.credentials.invalid");
        }
        return ResponseEntity.ok(authService.login(loginIdentifier, password));
    }
}