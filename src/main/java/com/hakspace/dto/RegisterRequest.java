package com.hakspace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "fullName.required")
    private String fullName;

    @NotBlank(message = "username.required")
    private String username;

    @NotBlank(message = "email.required")
    @Email(message = "email.invalid")
    private String email;

    @NotBlank(message = "password.required")
    private String password;

    @NotBlank(message = "specialization.required")
    private String specialization;

    private String generalSpecialization;

    private String specificSpecialization;

    @NotBlank(message = "phone.required")
    private String phone;

    private String whatsapp;

    private String bio;

    private String cvUrl;

    private String portfolioUrl;
}
