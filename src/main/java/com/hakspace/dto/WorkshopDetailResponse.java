package com.hakspace.dto;

import com.hakspace.model.Workshop;
import lombok.Data;

@Data
public class WorkshopDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String materialsLink;
    private String workshopDate;
    private String startTime;
    private String endTime;
    private String duration;
    private String instructorName;
    private Double price;
    private Integer maxCapacity;
    private Integer currentParticipants;
    private Integer remainingSeats;
    private String status;

    public static WorkshopDetailResponse from(Workshop w) {
        WorkshopDetailResponse dto = new WorkshopDetailResponse();
        dto.id = w.getId();
        dto.title = w.getTitle();
        dto.description = w.getDescription();
        dto.imageUrl = w.getImageUrl();
        dto.materialsLink = w.getMaterialsLink();
        dto.workshopDate = w.getWorkshopDate();
        dto.startTime = w.getStartTime();
        dto.endTime = w.getEndTime();
        dto.duration = w.getDuration();
        dto.instructorName = w.getInstructorName();
        dto.price = w.getPrice();
        dto.maxCapacity = w.getMaxCapacity() != null ? w.getMaxCapacity() : 30;
        dto.currentParticipants = w.getCurrentParticipants() != null ? w.getCurrentParticipants() : 0;
        dto.remainingSeats = Math.max(0, dto.maxCapacity - dto.currentParticipants);
        dto.status = w.getStatus() != null ? w.getStatus().name() : "ACTIVE";
        return dto;
    }
}
