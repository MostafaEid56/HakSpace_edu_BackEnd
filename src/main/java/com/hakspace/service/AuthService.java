package com.hakspace.service;

import com.hakspace.dto.RegisterRequest;
import com.hakspace.dto.UserResponse;
import com.hakspace.model.User;
import com.hakspace.repository.UserRepository;
import com.hakspace.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public UserResponse registerCommunityMember(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new RuntimeException("auth.email.exists");
        }
        if (req.getUsername() != null && userRepo.existsByUsername(req.getUsername())) {
            throw new RuntimeException("auth.username.exists");
        }

        User user = new User();
        user.setFullName(req.getFullName());
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPassword(encoder.encode(req.getPassword()));
        user.setSpecialization(req.getSpecialization());
        user.setPhone(req.getPhone());
        user.setWhatsapp(req.getWhatsapp() != null && !req.getWhatsapp().isBlank() ? req.getWhatsapp() : req.getPhone());
        user.setBio(req.getBio());
        user.setCvUrl(req.getCvUrl());
        user.setPortfolioUrl(req.getPortfolioUrl());
        user.setRole(User.Role.USER);

        User saved = userRepo.save(user);
        return UserResponse.from(saved);
    }

    public void register(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("auth.email.exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public Map<String, Object> login(String loginIdentifier, String password) {
        User user = userRepo.findByEmailOrUsername(loginIdentifier)
                .orElseThrow(() -> new RuntimeException("auth.credentials.invalid"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("auth.credentials.invalid");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return Map.of(
                "token", token,
                "user", UserResponse.from(user)
        );
    }
}
