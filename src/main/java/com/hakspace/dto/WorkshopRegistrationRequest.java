package com.hakspace.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkshopRegistrationRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String city;
    private String contactMethod = "WHATSAPP";
    private String contactTime = "ANYTIME";
    private String notes;

    @NotNull(message = "Workshop ID is required")
    private Long workshopId;
}
