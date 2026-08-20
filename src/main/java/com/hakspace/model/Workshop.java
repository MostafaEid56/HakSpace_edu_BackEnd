package com.hakspace.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "workshops")
public class Workshop {

    public enum WorkshopStatus { ACTIVE, IN_PROGRESS, PENDING, COMING_SOON, COMPLETED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
    private String materialsLink;
    private String workshopDate;
    private String startTime;
    private String endTime;
    private String duration;
    private String instructorName;
    private Double price = 0.0;
    
    @Column(name = "max_capacity")
    private Integer maxCapacity = 30;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkshopStatus status = WorkshopStatus.ACTIVE;
}
