package com.hakspace.dto;

import com.hakspace.model.Course;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Enriched course response including all groups with seat availability.
 */
@Data
public class CourseDetailResponse {

    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private String courseMaterialsLink;
    private String duration;
    private String instructorName;
    private Double price;
    private Double rating;
    private Integer studentCount;
    private Integer remainingSeats;
    private String status;
    private String paymentType;
    private Double downPayment;
    private Double installmentAmount;
    private String installmentFrequency;
    private Integer numberOfInstallments;
    private List<CourseGroupResponse> groups;

    public static CourseDetailResponse from(Course course) {
        CourseDetailResponse dto = new CourseDetailResponse();
        dto.id = course.getId();
        dto.title = course.getTitle();
        dto.description = course.getDescription();
        dto.imageUrl = course.getImageUrl();
        dto.courseMaterialsLink = course.getCourseMaterialsLink();
        dto.duration = course.getDuration();
        dto.instructorName = course.getInstructorName();
        dto.price = course.getPrice();
        dto.rating = course.getRating();
        dto.studentCount = course.getStudentCount();
        dto.status = course.getStatus() != null ? course.getStatus().name() : "ACTIVE";
        dto.paymentType = course.getPaymentType() != null ? course.getPaymentType().name() : "FULL_PAYMENT";
        dto.downPayment = course.getDownPayment();
        dto.installmentAmount = course.getInstallmentAmount();
        dto.installmentFrequency = course.getInstallmentFrequency();
        dto.numberOfInstallments = course.getNumberOfInstallments();
        dto.groups = course.getGroups().stream()
                .map(CourseGroupResponse::from)
                .collect(Collectors.toList());
        dto.remainingSeats = dto.groups.stream()
                .mapToInt(g -> g.getRemainingSeats() != null ? g.getRemainingSeats() : 0)
                .sum();
        return dto;
    }
}
