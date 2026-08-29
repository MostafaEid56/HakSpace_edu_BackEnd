package com.hakspace.config;

import com.hakspace.model.Course;
import com.hakspace.model.User;
import com.hakspace.repository.CourseRepository;
import com.hakspace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DbSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final com.hakspace.repository.WorkshopRepository workshopRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.hakspace.service.InvitationCodeService invitationCodeService;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void run(String... args) throws Exception {
        invitationCodeService.getActiveCode();

        // Ensure production admin user exists with original credentials
        User admin = userRepository.findByEmailOrUsername("admin@Hakss.com")
                .or(() -> userRepository.findByEmailOrUsername("admin@hakspace.com"))
                .or(() -> userRepository.findByEmailOrUsername("admin"))
                .orElse(null);

        if (admin == null) {
            admin = new User();
            admin.setEmail("admin@Hakss.com");
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Hakss@012Hvv"));
            admin.setFullName("HakSpace Admin");
            admin.setRole(User.Role.ADMIN);
            admin.setSpecialization("System Administrator");
            admin.setPhone("01000000000");
            admin.setBio("HakSpace Platform Administrator");
            userRepository.save(admin);
            System.out.println("Admin user seeded with production credentials: admin@Hakss.com / Hakss@012Hvv");
        } else {
            // Ensure username is populated for community lookup without altering existing password
            if (admin.getUsername() == null || admin.getUsername().isBlank()) {
                admin.setUsername("admin");
                userRepository.save(admin);
            }
        }

        // Seed courses
        if (courseRepository.count() == 0) {
            Course c1 = new Course();
            c1.setTitle("Full Stack Web Development BootCamp");
            c1.setDescription(
                    "Learn modern web development using HTML, CSS, React, Node.js, and PostgreSQL. Build real-world projects and deploy them.");
            c1.setImageUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97");
            c1.setDuration("12 Weeks");
            c1.setInstructorName("Dr. Sarah Jenkins");
            c1.setPrice(499.0);
            c1.setRating(4.8);
            c1.setStudentCount(120);
            courseRepository.save(c1);

            Course c2 = new Course();
            c2.setTitle("Data Science & Machine Learning");
            c2.setDescription(
                    "Master Python, Pandas, NumPy, Scikit-Learn, and TensorFlow. Dive deep into statistics, data visualization, and predictive modeling.");
            c2.setImageUrl("https://images.unsplash.com/photo-1527474305487-b87b222841cc");
            c2.setDuration("10 Weeks");
            c2.setInstructorName("Prof. Michael Chen");
            c2.setPrice(399.0);
            c2.setRating(4.9);
            c2.setStudentCount(85);
            courseRepository.save(c2);

            Course c3 = new Course();
            c3.setTitle("AI & Deep Learning Masterclass");
            c3.setDescription(
                    "Learn advanced neural networks, natural language processing, computer vision, and generative AI. Build and fine-tune large language models.");
            c3.setImageUrl("https://images.unsplash.com/photo-1677442136019-21780efad99a");
            c3.setDuration("8 Weeks");
            c3.setInstructorName("Alex Mercer");
            c3.setPrice(599.0);
            c3.setRating(4.7);
            c3.setStudentCount(95);
            courseRepository.save(c3);

            System.out.println("Default courses seeded successfully.");
        }

        // Seed workshops
        if (workshopRepository.count() == 0) {
            com.hakspace.model.Workshop w1 = new com.hakspace.model.Workshop();
            w1.setTitle("Generative AI & LLM Fine-Tuning Hands-on Workshop");
            w1.setDescription("An intensive 1-day workshop covering prompt engineering, RAG architecture, and fine-tuning Open-Source LLMs for enterprise applications.");
            w1.setImageUrl("https://images.unsplash.com/photo-1677442136019-21780efad99a?auto=format&fit=crop&w=800&q=80");
            w1.setWorkshopDate("2026-09-15");
            w1.setStartTime("18:00");
            w1.setEndTime("21:00");
            w1.setDuration("3 Hours");
            w1.setInstructorName("Alex Mercer");
            w1.setPrice(0.0);
            w1.setMaxCapacity(30);
            w1.setCurrentParticipants(12);
            w1.setStatus(com.hakspace.model.Workshop.WorkshopStatus.ACTIVE);
            workshopRepository.save(w1);

            com.hakspace.model.Workshop w2 = new com.hakspace.model.Workshop();
            w2.setTitle("Modern Microservices with Spring Boot & Docker");
            w2.setDescription("Learn to architect, containerize, and deploy resilient microservices with Spring Cloud, Docker, and Kubernetes.");
            w2.setImageUrl("https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=800&q=80");
            w2.setWorkshopDate("2026-09-22");
            w2.setStartTime("19:00");
            w2.setEndTime("22:00");
            w2.setDuration("3 Hours");
            w2.setInstructorName("Dr. Sarah Jenkins");
            w2.setPrice(0.0);
            w2.setMaxCapacity(25);
            w2.setCurrentParticipants(5);
            w2.setStatus(com.hakspace.model.Workshop.WorkshopStatus.COMING_SOON);
            workshopRepository.save(w2);

            System.out.println("Default workshops seeded successfully.");
        }
    }
}
