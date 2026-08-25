package com.hakspace.service;

import com.hakspace.dto.CommunityMemberDTO;
import com.hakspace.dto.UserDashboardResponse;
import com.hakspace.dto.UserResponse;
import com.hakspace.model.Course;
import com.hakspace.model.Enrollment;
import com.hakspace.model.StudentCourse;
import com.hakspace.model.User;
import com.hakspace.repository.CourseRepository;
import com.hakspace.repository.EnrollmentRepository;
import com.hakspace.repository.StudentCourseRepository;
import com.hakspace.repository.UserRepository;
import com.hakspace.repository.WorkshopRegistrationRepository;
import com.hakspace.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final StudentCourseRepository studentCourseRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final WorkshopRegistrationRepository workshopRegRepo;
    private final WorkshopRepository workshopRepo;

    @Transactional(readOnly = true)
    public UserDashboardResponse getUserDashboard(String login) {
        User user = userRepo.findByEmailOrUsername(login)
                .orElseThrow(() -> new RuntimeException("user.not.found"));

        UserDashboardResponse resp = new UserDashboardResponse();
        resp.setProfile(UserResponse.from(user));

        List<StudentCourse> studentCourses = studentCourseRepo.findByStudentId(user.getId());
        if (studentCourses.isEmpty() && user.getEmail() != null) {
            User emailUser = userRepo.findByEmail(user.getEmail()).orElse(null);
            if (emailUser != null && !emailUser.getId().equals(user.getId())) {
                studentCourses = studentCourseRepo.findByStudentId(emailUser.getId());
            }
        }
        List<Enrollment> enrollments = enrollmentRepo.findByUserId(user.getId());
        if (enrollments.isEmpty() && user.getEmail() != null) {
            enrollments = enrollmentRepo.findByEmail(user.getEmail());
        }

        List<UserDashboardResponse.CourseSummary> registered = new ArrayList<>();
        List<UserDashboardResponse.CourseSummary> inProgress = new ArrayList<>();
        List<UserDashboardResponse.CourseSummary> completed = new ArrayList<>();
        List<UserDashboardResponse.CourseSummary> upcoming = new ArrayList<>();
        Set<Long> handledCourseIds = new HashSet<>();

        for (StudentCourse sc : studentCourses) {
            Course c = sc.getCourse();
            if (c == null) continue;
            handledCourseIds.add(c.getId());

            UserDashboardResponse.CourseSummary summary = mapToCourseSummary(c);
            if (sc.getGroup() != null) {
                summary.setGroupName(sc.getGroup().getGroupName());
                summary.setSchedule(sc.getGroup().getSchedule());
            }
            if (sc.getEnrollmentDate() != null) {
                summary.setEnrollmentDate(sc.getEnrollmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            if (sc.getCompletionDate() != null) {
                summary.setCompletionDate(sc.getCompletionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            if (sc.getCertificate() != null) {
                summary.setCertificateCode(sc.getCertificate().getCertificateId());
            }

            if (sc.getCompletionStatus() == StudentCourse.CompletionStatus.COMPLETED) {
                summary.setStatus("COMPLETED");
                completed.add(summary);
            } else {
                summary.setStatus("IN_PROGRESS");
                inProgress.add(summary);
            }
        }

        for (Enrollment en : enrollments) {
            if (en.getStatus() == Enrollment.LeadStatus.ENROLLED || en.getStatus() == Enrollment.LeadStatus.CLOSED) {
                continue;
            }

            Course c = en.getCourse();
            if (c == null || handledCourseIds.contains(c.getId())) continue;
            handledCourseIds.add(c.getId());

            UserDashboardResponse.CourseSummary summary = mapToCourseSummary(c);
            if (en.getGroup() != null) {
                summary.setGroupName(en.getGroup().getGroupName());
                summary.setSchedule(en.getGroup().getSchedule());
            }
            if (en.getCreatedAt() != null) {
                summary.setEnrollmentDate(en.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            summary.setStatus("REGISTERED");
            registered.add(summary);
        }

        List<Course> allCourses = courseRepo.findAll();
        List<UserDashboardResponse.CourseSummary> available = allCourses.stream()
                .filter(c -> !handledCourseIds.contains(c.getId()))
                .map(this::mapToCourseSummary)
                .collect(Collectors.toList());

        resp.setRegisteredCourses(registered);
        resp.setInProgressCourses(inProgress);
        resp.setCompletedCourses(completed);
        resp.setUpcomingCourses(upcoming);
        resp.setAvailableCourses(available);

        // ── Workshops Section ──────────────────────────────────────────────────
        List<com.hakspace.model.WorkshopRegistration> wRegs = workshopRegRepo.findByUserId(user.getId());
        if (wRegs.isEmpty() && user.getEmail() != null) {
            wRegs = workshopRegRepo.findByEmail(user.getEmail());
        }

        wRegs = wRegs.stream()
                .filter(wr -> wr.getStatus() == com.hakspace.model.WorkshopRegistration.RegistrationStatus.CONFIRMED)
                .collect(Collectors.toList());

        List<UserDashboardResponse.WorkshopSummary> inProgressW = new ArrayList<>();
        List<UserDashboardResponse.WorkshopSummary> completedW = new ArrayList<>();
        List<UserDashboardResponse.WorkshopSummary> upcomingW = new ArrayList<>();
        Set<Long> handledWorkshopIds = new HashSet<>();

        for (com.hakspace.model.WorkshopRegistration wr : wRegs) {
            com.hakspace.model.Workshop w = wr.getWorkshop();
            if (w == null || handledWorkshopIds.contains(w.getId())) continue;
            handledWorkshopIds.add(w.getId());

            UserDashboardResponse.WorkshopSummary ws = mapToWorkshopSummary(w);
            if (wr.getCreatedAt() != null) {
                ws.setRegistrationDate(wr.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            if (w.getStatus() == com.hakspace.model.Workshop.WorkshopStatus.COMPLETED) {
                completedW.add(ws);
            } else if (w.getStatus() == com.hakspace.model.Workshop.WorkshopStatus.IN_PROGRESS) {
                inProgressW.add(ws);
            } else {
                upcomingW.add(ws);
            }
        }

        List<com.hakspace.model.Workshop> allWorkshops = workshopRepo.findAll();
        List<UserDashboardResponse.WorkshopSummary> availableW = allWorkshops.stream()
                .filter(w -> !handledWorkshopIds.contains(w.getId()))
                .map(this::mapToWorkshopSummary)
                .collect(Collectors.toList());

        resp.setInProgressWorkshops(inProgressW);
        resp.setCompletedWorkshops(completedW);
        resp.setUpcomingWorkshops(upcomingW);
        resp.setAvailableWorkshops(availableW);

        return resp;
    }

    @Transactional(readOnly = true)
    public UserDashboardResponse getPublicUserDashboard(String targetUsername, String requesterLogin) {
        UserDashboardResponse resp = getUserDashboard(targetUsername);
        
        boolean isSelf = false;
        if (requesterLogin != null && !requesterLogin.isBlank()) {
            User requester = userRepo.findByEmailOrUsername(requesterLogin).orElse(null);
            if (requester != null && resp.getProfile() != null && requester.getId().equals(resp.getProfile().getId())) {
                isSelf = true;
            }
        }

        if (!isSelf && resp.getProfile() != null) {
            resp.getProfile().setPhone(null);
            resp.getProfile().setWhatsapp(null);
            resp.getProfile().setEmail(null);
        }

        return resp;
    }

    @Transactional
    public UserResponse updateProfile(String login, UserResponse req) {
        User user = userRepo.findByEmailOrUsername(login)
                .orElseThrow(() -> new RuntimeException("user.not.found"));

        if (req.getFullName() != null && !req.getFullName().isBlank()) {
            user.setFullName(req.getFullName());
        }
        if (req.getSpecialization() != null) user.setSpecialization(req.getSpecialization());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getWhatsapp() != null) user.setWhatsapp(req.getWhatsapp());
        if (req.getBio() != null) user.setBio(req.getBio());
        if (req.getCvUrl() != null) user.setCvUrl(req.getCvUrl());
        if (req.getPortfolioUrl() != null) user.setPortfolioUrl(req.getPortfolioUrl());

        return UserResponse.from(userRepo.save(user));
    }

    @Transactional(readOnly = true)
    public List<CommunityMemberDTO> getCommunityMembers(String specialization, String query) {
        List<User> users = userRepo.searchCommunityMembers(specialization, query);
        List<CommunityMemberDTO> members = new ArrayList<>();

        for (User u : users) {
            List<StudentCourse> studentCourses = studentCourseRepo.findByStudentId(u.getId());
            List<Enrollment> enrollments = enrollmentRepo.findByUserId(u.getId());
            if (enrollments.isEmpty() && u.getEmail() != null) {
                enrollments = enrollmentRepo.findByEmail(u.getEmail());
            }

            List<CommunityMemberDTO.MemberCourse> courseList = new ArrayList<>();
            Set<Long> courseIds = new HashSet<>();

            for (StudentCourse sc : studentCourses) {
                if (sc.getCourse() == null) continue;
                courseIds.add(sc.getCourse().getId());
                CommunityMemberDTO.MemberCourse mc = new CommunityMemberDTO.MemberCourse();
                mc.setCourseId(sc.getCourse().getId());
                mc.setCourseTitle(sc.getCourse().getTitle());
                if (sc.getGroup() != null) mc.setGroupName(sc.getGroup().getGroupName());
                mc.setStatus(sc.getCompletionStatus() == StudentCourse.CompletionStatus.COMPLETED ? "COMPLETED" : "IN_PROGRESS");
                if (sc.getCertificate() != null) mc.setCertificateCode(sc.getCertificate().getCertificateId());
                courseList.add(mc);
            }

            for (Enrollment en : enrollments) {
                if (en.getCourse() == null || courseIds.contains(en.getCourse().getId())) continue;
                courseIds.add(en.getCourse().getId());
                CommunityMemberDTO.MemberCourse mc = new CommunityMemberDTO.MemberCourse();
                mc.setCourseId(en.getCourse().getId());
                mc.setCourseTitle(en.getCourse().getTitle());
                if (en.getGroup() != null) mc.setGroupName(en.getGroup().getGroupName());
                mc.setStatus("UPCOMING");
                courseList.add(mc);
            }

            members.add(CommunityMemberDTO.fromUser(u, courseList));
        }

        return members;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recalculateBadge(User student) {
        if (student == null || student.getId() == null) return;
        // Re-fetch by ID so this new transaction has a clean, managed entity
        User managed = userRepo.findById(student.getId()).orElse(null);
        if (managed == null) return;
        long enrolledCount = studentCourseRepo.countByStudentId(managed.getId());
        if (enrolledCount >= 2) {
            managed.setBadge(User.Badge.GOLD);
        } else if (enrolledCount == 1) {
            managed.setBadge(User.Badge.BRONZE);
        } else {
            managed.setBadge(User.Badge.SILVER);
        }
        userRepo.save(managed);
    }

    @Transactional
    public UserResponse updateBadge(Long userId, String rawBadge) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("user.not.found"));
        try {
            User.Badge newBadge = User.Badge.valueOf(rawBadge.toUpperCase());
            user.setBadge(newBadge);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("user.badge.invalid");
        }
        return UserResponse.from(userRepo.save(user));
    }

    private UserDashboardResponse.CourseSummary mapToCourseSummary(Course c) {
        UserDashboardResponse.CourseSummary summary = new UserDashboardResponse.CourseSummary();
        summary.setId(c.getId());
        summary.setTitle(c.getTitle());
        summary.setDescription(c.getDescription());
        summary.setImageUrl(c.getImageUrl());
        summary.setDuration(c.getDuration());
        summary.setInstructorName(c.getInstructorName());
        summary.setPrice(c.getPrice());
        summary.setStatus(c.getStatus() != null ? c.getStatus().name() : "ACTIVE");
        return summary;
    }

    private UserDashboardResponse.WorkshopSummary mapToWorkshopSummary(com.hakspace.model.Workshop w) {
        UserDashboardResponse.WorkshopSummary summary = new UserDashboardResponse.WorkshopSummary();
        summary.setId(w.getId());
        summary.setTitle(w.getTitle());
        summary.setDescription(w.getDescription());
        summary.setImageUrl(w.getImageUrl());
        summary.setWorkshopDate(w.getWorkshopDate());
        summary.setStartTime(w.getStartTime());
        summary.setEndTime(w.getEndTime());
        summary.setDuration(w.getDuration());
        summary.setInstructorName(w.getInstructorName());
        summary.setPrice(w.getPrice());
        summary.setMaxCapacity(w.getMaxCapacity() != null ? w.getMaxCapacity() : 30);
        summary.setCurrentParticipants(w.getCurrentParticipants() != null ? w.getCurrentParticipants() : 0);
        summary.setRemainingSeats(Math.max(0, summary.getMaxCapacity() - summary.getCurrentParticipants()));
        summary.setStatus(w.getStatus() != null ? w.getStatus().name() : "ACTIVE");
        return summary;
    }
}
