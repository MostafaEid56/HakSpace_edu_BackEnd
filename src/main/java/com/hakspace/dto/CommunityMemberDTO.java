package com.hakspace.dto;

import com.hakspace.model.User;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class CommunityMemberDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String specialization;
    private String phone;
    private String whatsapp;
    private String bio;
    private String cvUrl;
    private String portfolioUrl;
    private String joinedDate;
    private User.Badge badge;
    private List<MemberCourse> courses;

    @Data
    public static class MemberCourse {
        private Long courseId;
        private String courseTitle;
        private String groupName;
        private String status; // IN_PROGRESS, UPCOMING, COMPLETED
        private String certificateCode;
    }

    public static CommunityMemberDTO fromUser(User user, List<MemberCourse> courses) {
        CommunityMemberDTO dto = new CommunityMemberDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setSpecialization(user.getSpecialization());
        dto.setPhone(user.getPhone());
        dto.setWhatsapp(user.getWhatsapp() != null ? user.getWhatsapp() : user.getPhone());
        dto.setBio(user.getBio());
        dto.setCvUrl(user.getCvUrl());
        dto.setPortfolioUrl(user.getPortfolioUrl());
        dto.setBadge(user.getBadge() != null ? user.getBadge() : User.Badge.SILVER);
        if (user.getCreatedAt() != null) {
            dto.setJoinedDate(user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        dto.setCourses(courses);
        return dto;
    }
}
