package com.hakspace.controller;

import com.hakspace.dto.CommunityMemberDTO;
import com.hakspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/community")
@RequiredArgsConstructor
public class AdminCommunityController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<CommunityMemberDTO>> getCommunityMembers(
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String query) {
        return ResponseEntity.ok(userService.getCommunityMembers(specialization, query));
    }

    @PutMapping("/{userId}/badge")
    public ResponseEntity<com.hakspace.dto.UserResponse> updateBadge(
            @PathVariable Long userId,
            @RequestBody java.util.Map<String, String> body) {
        String badge = body.get("badge");
        return ResponseEntity.ok(userService.updateBadge(userId, badge));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> removeUserFromCommunity(
            @PathVariable Long userId,
            org.springframework.security.core.Authentication auth) {
        String adminLogin = (auth != null) ? auth.getName() : null;
        userService.deleteUser(userId, adminLogin);
        return ResponseEntity.ok(java.util.Map.of(
                "success", true,
                "message", "user.delete.success"
        ));
    }
}
