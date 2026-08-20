package com.hakspace.model;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "full_name")
    private String fullName;

    private String specialization;
    private String phone;
    private String whatsapp;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "cv_url")
    private String cvUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    private Badge badge = Badge.SILVER;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Role { USER, ADMIN }
    public enum Badge { SILVER, BRONZE, GOLD }
}