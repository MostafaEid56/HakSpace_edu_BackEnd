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
}
