package com.hakspace.dto;

import com.hakspace.model.InstructorProfile;
import com.hakspace.model.User;
import lombok.Data;
import java.util.List;

@Data
public class InstructorProfileResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String username;
    private String email;
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

    // Courses taught list (injected at service layer)
    private List<CourseItem> courses;

    @Data
    public static class CourseItem {
        private Long id;
        private String title;
        private String description;
        private String imageUrl;
        private String duration;
        private Double price;
        private String status;
        private Double rating;
        private Integer studentCount;
    }

    public static InstructorProfileResponse from(InstructorProfile profile) {
        InstructorProfileResponse resp = new InstructorProfileResponse();
        User user = profile.getUser();
        resp.setId(profile.getId());
        resp.setUserId(user.getId());
        resp.setFullName(user.getFullName());
        resp.setUsername(user.getUsername());
        resp.setEmail(user.getEmail());
        resp.setJobTitle(profile.getJobTitle());
        resp.setShortBio(profile.getShortBio());
        resp.setBio(profile.getBio());
        resp.setSpecialization(profile.getSpecialization() != null
                ? profile.getSpecialization() : user.getSpecialization());
        resp.setYearsExperience(profile.getYearsExperience());
        resp.setProfileImageUrl(profile.getProfileImageUrl());
        resp.setLinkedinUrl(profile.getLinkedinUrl());
        resp.setGithubUrl(profile.getGithubUrl());
        resp.setFacebookUrl(profile.getFacebookUrl());
        resp.setTwitterUrl(profile.getTwitterUrl());
        resp.setWebsiteUrl(profile.getWebsiteUrl());
        return resp;
    }
}
