package com.hakspace.service;

import com.hakspace.dto.InstructorProfileRequest;
import com.hakspace.dto.InstructorProfileResponse;
import com.hakspace.model.Course;
import com.hakspace.model.InstructorProfile;
import com.hakspace.model.User;
import com.hakspace.repository.CourseRepository;
import com.hakspace.repository.InstructorProfileRepository;
import com.hakspace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorProfileRepository profileRepo;
    private final UserRepository userRepo;
    private final CourseRepository courseRepo;
    private final ImageStorageService imageStorageService;

    // ── Public ─────────────────────────────────────────────────────────────────

    public List<InstructorProfileResponse> getAllInstructors() {
        return profileRepo.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getUser().getIsInstructor()))
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    public InstructorProfileResponse getByUsername(String username) {
        InstructorProfile profile = profileRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("instructor.not_found"));
        return buildResponse(profile);
    }

    // ── Admin ──────────────────────────────────────────────────────────────────

    @Transactional
    public InstructorProfileResponse promoteToInstructor(Long userId, InstructorProfileRequest req) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("user.not.found"));

        user.setIsInstructor(true);
        userRepo.save(user);

        InstructorProfile profile;
        if (profileRepo.existsByUserId(userId)) {
            profile = profileRepo.findByUserId(userId).get();
        } else {
            profile = new InstructorProfile();
            profile.setUser(user);
        }

        applyRequest(profile, req);
        profileRepo.save(profile);
        return buildResponse(profile);
    }

    @Transactional
    public InstructorProfileResponse updateProfile(Long userId, InstructorProfileRequest req) {
        InstructorProfile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("instructor.not_found"));
        applyRequest(profile, req);
        profileRepo.save(profile);
        return buildResponse(profile);
    }

    @Transactional
    public void demoteInstructor(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("user.not.found"));
        user.setIsInstructor(false);
        userRepo.save(user);
    }

    @Transactional
    public InstructorProfileResponse uploadProfileImage(Long userId, org.springframework.web.multipart.MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("image.empty");
        }

        // Validate size (5MB limit)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("image.too_large");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/webp"))) {
            throw new RuntimeException("image.invalid_format");
        }

        InstructorProfile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("instructor.not_found"));

        String oldImageUrl = profile.getProfileImageUrl();
        String newImageUrl = imageStorageService.upload(file);

        profile.setProfileImageUrl(newImageUrl);
        profileRepo.save(profile);

        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
            imageStorageService.delete(oldImageUrl);
        }

        return buildResponse(profile);
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private void applyRequest(InstructorProfile profile, InstructorProfileRequest req) {
        if (req == null) return;
        if (req.getJobTitle() != null)       profile.setJobTitle(req.getJobTitle());
        if (req.getShortBio() != null)       profile.setShortBio(req.getShortBio());
        if (req.getBio() != null)            profile.setBio(req.getBio());
        if (req.getSpecialization() != null) profile.setSpecialization(req.getSpecialization());
        if (req.getYearsExperience() != null) profile.setYearsExperience(req.getYearsExperience());
        if (req.getProfileImageUrl() != null) profile.setProfileImageUrl(req.getProfileImageUrl());
        if (req.getLinkedinUrl() != null)    profile.setLinkedinUrl(req.getLinkedinUrl());
        if (req.getGithubUrl() != null)      profile.setGithubUrl(req.getGithubUrl());
        if (req.getFacebookUrl() != null)    profile.setFacebookUrl(req.getFacebookUrl());
        if (req.getTwitterUrl() != null)     profile.setTwitterUrl(req.getTwitterUrl());
        if (req.getWebsiteUrl() != null)     profile.setWebsiteUrl(req.getWebsiteUrl());
    }

    private InstructorProfileResponse buildResponse(InstructorProfile profile) {
        InstructorProfileResponse resp = InstructorProfileResponse.from(profile);
        String username = profile.getUser().getUsername();
        String name = profile.getUser().getFullName();

        // Fetch courses by username first, fallback to instructorName
        List<Course> courses = username != null
                ? courseRepo.findByInstructorUsernameIgnoreCase(username)
                : new ArrayList<>();
        if (courses.isEmpty() && name != null) {
            courses = courseRepo.findByInstructorNameIgnoreCase(name);
        }

        List<InstructorProfileResponse.CourseItem> items = courses.stream().map(c -> {
            InstructorProfileResponse.CourseItem item = new InstructorProfileResponse.CourseItem();
            item.setId(c.getId());
            item.setTitle(c.getTitle());
            item.setDescription(c.getDescription());
            item.setImageUrl(c.getImageUrl());
            item.setDuration(c.getDuration());
            item.setPrice(c.getPrice());
            item.setStatus(c.getStatus() != null ? c.getStatus().name() : "ACTIVE");
            item.setRating(c.getRating());
            item.setStudentCount(c.getStudentCount());
            return item;
        }).collect(Collectors.toList());

        resp.setCourses(items);
        return resp;
    }
}
