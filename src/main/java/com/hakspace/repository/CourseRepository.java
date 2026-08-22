package com.hakspace.repository;
import com.hakspace.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByInstructorUsernameIgnoreCase(String instructorUsername);
    List<Course> findByInstructorNameIgnoreCase(String instructorName);
}