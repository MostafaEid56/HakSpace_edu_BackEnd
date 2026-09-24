-- V16: Resynchronize course_groups current_students and is_available based on student_courses
-- Fixes historical count discrepancies caused by deleting converted lead records
-- Uses LEAST(count, max_students) and GREATEST(..., 0) to guarantee chk_students constraint is never violated

UPDATE course_groups cg
SET current_students = LEAST(
    cg.max_students,
    GREATEST(
        0,
        (
            SELECT COUNT(*)
            FROM student_courses sc
            WHERE sc.group_id = cg.id
        )
    )
);

UPDATE course_groups
SET is_available = (current_students < max_students);

