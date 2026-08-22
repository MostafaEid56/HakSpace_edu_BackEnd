package com.hakspace.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    public enum CourseStatus { ACTIVE, IN_PROGRESS, PENDING, COMING_SOON, COMPLETED }
    public enum PaymentType { FULL_PAYMENT, INSTALLMENT }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
    private String courseMaterialsLink;
    private String duration;
    private String instructorName;
    private Double price;
    private Double rating = 0.0;
    private Integer studentCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType = PaymentType.FULL_PAYMENT;

    private Double downPayment;
    private Double installmentAmount;
    private String installmentFrequency;
    private Integer numberOfInstallments;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status = CourseStatus.ACTIVE;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseGroup> groups = new ArrayList<>();
}