package com.hakspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BlacklistRequest {

    private String fullName;

    @Pattern(regexp = "^[+\\d][\\d\\s()-]{6,20}$", message = "Invalid phone number format")
    private String phoneNumber;

    private String email;

    private String nationalId;

    @NotBlank(message = "Reason is required")
    private String reason;
}
