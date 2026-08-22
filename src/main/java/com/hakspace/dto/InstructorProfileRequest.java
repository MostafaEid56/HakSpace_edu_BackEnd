package com.hakspace.dto;

import lombok.Data;

@Data
public class InstructorProfileRequest {
    private String jobTitle;
    private String shortBio;
    private String bio;
    private String specialization;
    private Integer yearsExperience;
    private String profileImageUrl;
    private String linkedinUrl;
    private String githubUrl;
    private String facebookUrl;
    private String twitterUrl;
    private String websiteUrl;
}
