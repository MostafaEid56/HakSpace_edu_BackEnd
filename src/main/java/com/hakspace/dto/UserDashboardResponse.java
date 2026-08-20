package com.hakspace.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDashboardResponse {
    private UserResponse profile;
    private List<CourseSummary> inProgressCourses;
    private List<CourseSummary> upcomingCourses;
    private List<CourseSummary> completedCourses;
    private List<CourseSummary> availableCourses;

    private List<WorkshopSummary> inProgressWorkshops;
    private List<WorkshopSummary> upcomingWorkshops;
    private List<WorkshopSummary> completedWorkshops;
    private List<WorkshopSummary> availableWorkshops;

    @Data
    public static class CourseSummary {
        private Long id;
        private String title;
        private String description;
        private String imageUrl;
        private String duration;
        private String instructorName;
        private Double price;
        private String status;
        private String groupName;
        private String schedule;
        private String enrollmentDate;
        private String completionDate;
        private String certificateCode;
    }

    @Data
    public static class WorkshopSummary {
        private Long id;
        private String title;
        private String description;
        private String imageUrl;
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
        private String registrationDate;
    }
}
