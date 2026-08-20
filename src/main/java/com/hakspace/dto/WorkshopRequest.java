package com.hakspace.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WorkshopRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String imageUrl;
    private String materialsLink;
    private String workshopDate;
    private String startTime;
    private String endTime;
    private String duration;
    private String instructorName;
    private Double price = 0.0;
    private Integer maxCapacity = 30;
    private String status = "ACTIVE";
}
