package com.hakspace.service;

import com.hakspace.model.Enrollment;
import com.hakspace.model.Enrollment.LeadStatus;
import com.hakspace.model.StudentCourse;
import com.hakspace.model.User;
import com.hakspace.repository.CourseGroupRepository;
import com.hakspace.repository.EnrollmentRepository;
import com.hakspace.repository.StudentCourseRepository;
import com.hakspace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseGroupRepository groupRepo;
    private final UserRepository userRepo;
    private final StudentCourseRepository studentCourseRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public List<Enrollment> getAll(Long courseId) {
        if (courseId != null) {
            return enrollmentRepo.findByCourseId(courseId);
        }
        return enrollmentRepo.findAll();
    }

    @Transactional
    public Enrollment updateStatus(Long id, String rawStatus) {
        if (rawStatus == null) {
            throw new RuntimeException("enrollment.status.required");
        }

        LeadStatus newStatus;
        try {
            newStatus = LeadStatus.valueOf(rawStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("enrollment.status.invalid");
        }

        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("enrollment.not.found"));

        LeadStatus oldStatus = enrollment.getStatus();
        Long groupId = (enrollment.getGroup() != null) ? enrollment.getGroup().getId() : null;

        // ── Seat counter side-effects ─────────────────────────────────────────
        if (groupId != null) {
            boolean approving = (newStatus == LeadStatus.ENROLLED && oldStatus != LeadStatus.ENROLLED);
            boolean cancelling = (oldStatus == LeadStatus.ENROLLED && newStatus != LeadStatus.ENROLLED);

            if (approving) {
                User student = enrollment.getUser();
                if (student == null && enrollment.getEmail() != null) {
                    student = userRepo.findByEmail(enrollment.getEmail()).orElse(null);
                }
                if (student == null && enrollment.getPhone() != null) {
                    student = userRepo.findByEmail(enrollment.getPhone()).orElse(null);
                }
                if (student == null) {
                    student = new User();
                    student.setEmail(enrollment.getEmail() != null ? enrollment.getEmail() : enrollment.getPhone());
                    student.setFullName(enrollment.getFullName());
                    student.setPassword(passwordEncoder.encode("123456"));
                    student.setRole(User.Role.USER);
                    student = userRepo.save(student);
                }

                // Verify duplicate enrollment before incrementing count
                if (!studentCourseRepo.existsByStudentIdAndCourseId(student.getId(), enrollment.getCourse().getId())) {
                    int updated = groupRepo.incrementStudentCount(groupId);
                    if (updated == 0) {
                        throw new RuntimeException("enrollment.group.full");
                    }

                    StudentCourse studentCourse = new StudentCourse();
                    studentCourse.setStudent(student);
                    studentCourse.setCourse(enrollment.getCourse());
                    studentCourse.setGroup(enrollment.getGroup());
                    studentCourse.setEnrollmentDate(LocalDateTime.now());
                    studentCourse.setCompletionStatus(StudentCourse.CompletionStatus.IN_PROGRESS);
                    studentCourseRepo.save(studentCourse);

                    try {
                        userService.recalculateBadge(student);
                    } catch (Exception e) {
                        // Badge update failure must never break enrollment
                        System.err.println("[WARN] Badge recalculation failed for student " + student.getId() + ": " + e.getMessage());
                    }
                } else {
                    // Silently ignore or throw exception if already enrolled,
                    // but we MUST NOT increment the student count
                }
            } else if (cancelling) {
                groupRepo.decrementStudentCount(groupId);
            }
        }

        enrollment.setStatus(newStatus);
        return enrollmentRepo.save(enrollment);
    }

    @Transactional
    public void delete(Long id) {
        Enrollment enrollment = enrollmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("enrollment.not.found"));

        if (enrollment.getStatus() == LeadStatus.ENROLLED && enrollment.getGroup() != null) {
            groupRepo.decrementStudentCount(enrollment.getGroup().getId());
        }

        enrollmentRepo.delete(enrollment);
    }
}
