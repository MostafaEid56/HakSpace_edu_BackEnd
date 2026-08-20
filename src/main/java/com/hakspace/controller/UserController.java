package com.hakspace.controller;

import com.hakspace.dto.UserDashboardResponse;
import com.hakspace.dto.UserResponse;
import com.hakspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDashboardResponse> getProfile(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userService.getUserDashboard(auth.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(Authentication auth, @RequestBody UserResponse req) {
        if (auth == null || auth.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userService.updateProfile(auth.getName(), req));
    }
}
