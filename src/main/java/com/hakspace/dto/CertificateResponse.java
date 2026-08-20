package com.hakspace.dto;

import com.hakspace.model.Certificate;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CertificateResponse {
    private String certificateId;
    private String studentName;
    private String courseName;
    private String instructorName;
    private String courseDuration;
    private LocalDateTime startDate;
    private LocalDateTime issueDate;

    public static CertificateResponse from(Certificate cert) {
        CertificateResponse resp = new CertificateResponse();
        resp.setCertificateId(cert.getCertificateId());
        resp.setStudentName(cert.getStudentName());
        resp.setCourseName(cert.getCourseName());
        resp.setInstructorName(cert.getCourse() != null ? cert.getCourse().getInstructorName() : null);
        resp.setCourseDuration(cert.getCourse() != null ? cert.getCourse().getDuration() : null);
        resp.setStartDate(cert.getStudentCourse() != null ? cert.getStudentCourse().getEnrollmentDate() : null);
        resp.setIssueDate(cert.getIssueDate());
        return resp;
    }
}
