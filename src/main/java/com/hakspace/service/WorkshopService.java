package com.hakspace.service;

import com.hakspace.dto.WorkshopDetailResponse;
import com.hakspace.dto.WorkshopRegistrationRequest;
import com.hakspace.dto.WorkshopRequest;
import com.hakspace.model.User;
import com.hakspace.model.Workshop;
import com.hakspace.model.WorkshopRegistration;
import com.hakspace.repository.UserRepository;
import com.hakspace.repository.WorkshopRegistrationRepository;
import com.hakspace.repository.WorkshopRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkshopService {

    private final WorkshopRepository workshopRepo;
    private final WorkshopRegistrationRepository regRepo;
    private final UserRepository userRepo;

    // ── Public API ─────────────────────────────────────────────────────────────

    public List<WorkshopDetailResponse> getAll() {
        return workshopRepo.findAll().stream()
                .map(WorkshopDetailResponse::from)
                .collect(Collectors.toList());
    }

    public WorkshopDetailResponse getById(Long id) {
        Workshop workshop = workshopRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("workshop.not_found"));
        return WorkshopDetailResponse.from(workshop);
    }

    @Transactional
    public WorkshopRegistration register(@Valid WorkshopRegistrationRequest req, String userLogin) {
        Workshop workshop = workshopRepo.findById(req.getWorkshopId())
                .orElseThrow(() -> new RuntimeException("workshop.not_found"));

        if (workshop.getStatus() != Workshop.WorkshopStatus.ACTIVE) {
            throw new RuntimeException("workshop.registration.not_active");
        }

        if (workshop.getCurrentParticipants() >= workshop.getMaxCapacity()) {
            throw new RuntimeException("workshop.fully_booked");
        }

        User user = null;
        if (userLogin != null && !userLogin.isBlank()) {
            user = userRepo.findByEmailOrUsername(userLogin).orElse(null);
        }
        if (user == null && req.getEmail() != null) {
            user = userRepo.findByEmail(req.getEmail()).orElse(null);
        }

        WorkshopRegistration reg = new WorkshopRegistration();
        reg.setFullName(req.getFullName());
        reg.setPhone(req.getPhone());
        reg.setEmail(req.getEmail());
        reg.setCity(req.getCity());
        reg.setContactMethod(req.getContactMethod());
        reg.setContactTime(req.getContactTime());
        reg.setNotes(req.getNotes());
        reg.setWorkshop(workshop);
        reg.setUser(user);
        reg.setStatus(WorkshopRegistration.RegistrationStatus.NEW);

        return regRepo.save(reg);
    }

    // ── Admin API ──────────────────────────────────────────────────────────────

    public List<WorkshopDetailResponse> adminGetAll() {
        return workshopRepo.findAll().stream()
                .map(WorkshopDetailResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkshopDetailResponse create(@Valid WorkshopRequest req) {
        Workshop w = new Workshop();
        mapRequestToEntity(req, w);
        return WorkshopDetailResponse.from(workshopRepo.save(w));
    }

    @Transactional
    public WorkshopDetailResponse update(Long id, @Valid WorkshopRequest req) {
        Workshop w = workshopRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("workshop.not_found"));
        mapRequestToEntity(req, w);
        return WorkshopDetailResponse.from(workshopRepo.save(w));
    }

    @Transactional
    public void delete(Long id) {
        if (!workshopRepo.existsById(id)) {
            throw new RuntimeException("workshop.not_found");
        }
        workshopRepo.deleteById(id);
    }

    @Transactional
    public WorkshopDetailResponse updateStatus(Long id, String statusStr) {
        Workshop w = workshopRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("workshop.not_found"));
        try {
            Workshop.WorkshopStatus newStatus = Workshop.WorkshopStatus.valueOf(statusStr.toUpperCase());
            w.setStatus(newStatus);
            return WorkshopDetailResponse.from(workshopRepo.save(w));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("workshop.invalid_status");
        }
    }

    public List<WorkshopRegistration> getRegistrations(Long workshopId) {
        return regRepo.findByWorkshopId(workshopId);
    }

    @Transactional
    public WorkshopRegistration updateRegistrationStatus(Long regId, String statusStr) {
        WorkshopRegistration reg = regRepo.findById(regId)
                .orElseThrow(() -> new RuntimeException("registration.not_found"));
        
        WorkshopRegistration.RegistrationStatus newStatus;
        try {
            newStatus = WorkshopRegistration.RegistrationStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("registration.invalid_status");
        }

        WorkshopRegistration.RegistrationStatus oldStatus = reg.getStatus();
        if (newStatus != oldStatus) {
            Workshop workshop = reg.getWorkshop();
            boolean approving = (newStatus == WorkshopRegistration.RegistrationStatus.CONFIRMED && oldStatus != WorkshopRegistration.RegistrationStatus.CONFIRMED);
            boolean cancelling = (oldStatus == WorkshopRegistration.RegistrationStatus.CONFIRMED && newStatus != WorkshopRegistration.RegistrationStatus.CONFIRMED);

            if (approving) {
                if (workshop.getCurrentParticipants() >= workshop.getMaxCapacity()) {
                    throw new RuntimeException("workshop.fully_booked");
                }
                workshop.setCurrentParticipants(workshop.getCurrentParticipants() + 1);
                workshopRepo.save(workshop);
            } else if (cancelling) {
                workshop.setCurrentParticipants(Math.max(0, workshop.getCurrentParticipants() - 1));
                workshopRepo.save(workshop);
            }
        }

        reg.setStatus(newStatus);
        return regRepo.save(reg);
    }

    @Transactional
    public void deleteRegistration(Long regId) {
        WorkshopRegistration reg = regRepo.findById(regId)
                .orElseThrow(() -> new RuntimeException("registration.not_found"));

        if (reg.getStatus() == WorkshopRegistration.RegistrationStatus.CONFIRMED) {
            Workshop workshop = reg.getWorkshop();
            if (workshop != null) {
                workshop.setCurrentParticipants(Math.max(0, workshop.getCurrentParticipants() - 1));
                workshopRepo.save(workshop);
            }
        }

        regRepo.delete(reg);
    }

    private void mapRequestToEntity(WorkshopRequest req, Workshop w) {
        w.setTitle(req.getTitle());
        w.setDescription(req.getDescription());
        w.setImageUrl(req.getImageUrl());
        w.setMaterialsLink(req.getMaterialsLink());
        w.setWorkshopDate(req.getWorkshopDate());
        w.setStartTime(req.getStartTime());
        w.setEndTime(req.getEndTime());
        w.setDuration(req.getDuration());
        w.setInstructorName(req.getInstructorName());
        w.setPrice(req.getPrice() != null ? req.getPrice() : 0.0);
        w.setMaxCapacity(req.getMaxCapacity() != null ? req.getMaxCapacity() : 30);
        if (req.getStatus() != null) {
            try {
                w.setStatus(Workshop.WorkshopStatus.valueOf(req.getStatus().toUpperCase()));
            } catch (IllegalArgumentException ignored) {}
        }
    }
}
