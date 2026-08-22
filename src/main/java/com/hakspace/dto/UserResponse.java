package com.hakspace.dto;

import com.hakspace.model.User;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String specialization;
    private String phone;
    private String whatsapp;
    private String bio;
    private String cvUrl;
    private String portfolioUrl;
    private User.Role role;
    private User.Badge badge;
    private Boolean isInstructor;

    public static UserResponse from(User user) {
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setFullName(user.getFullName());
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setSpecialization(user.getSpecialization());
        resp.setPhone(user.getPhone());
        resp.setWhatsapp(user.getWhatsapp() != null ? user.getWhatsapp() : user.getPhone());
        resp.setBio(user.getBio());
        resp.setCvUrl(user.getCvUrl());
        resp.setPortfolioUrl(user.getPortfolioUrl());
        resp.setRole(user.getRole());
        resp.setBadge(user.getBadge() != null ? user.getBadge() : User.Badge.SILVER);
        resp.setIsInstructor(Boolean.TRUE.equals(user.getIsInstructor()));
        return resp;
    }
}
