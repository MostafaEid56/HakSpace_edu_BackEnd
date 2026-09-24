-- V16: Resynchronize course_groups current_students and is_available based on student_courses
-- Fixes historical count discrepancies caused by deleting converted lead records

UPDATE course_groups cg
SET current_students = (
    SELECT COUNT(*)
    FROM student_courses sc
    WHERE sc.group_id = cg.id
);

UPDATE course_groups
SET is_available = (current_students < max_students);
