package com.hakspace.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "instructor_profiles")
public class InstructorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "short_bio", length = 500)
    private String shortBio;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String specialization;

    @Column(name = "years_experience")
    private Integer yearsExperience = 0;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(name = "linkedin_url", columnDefinition = "TEXT")
    private String linkedinUrl;

    @Column(name = "github_url", columnDefinition = "TEXT")
    private String githubUrl;

    @Column(name = "facebook_url", columnDefinition = "TEXT")
    private String facebookUrl;

    @Column(name = "twitter_url", columnDefinition = "TEXT")
    private String twitterUrl;

    @Column(name = "website_url", columnDefinition = "TEXT")
    private String websiteUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
