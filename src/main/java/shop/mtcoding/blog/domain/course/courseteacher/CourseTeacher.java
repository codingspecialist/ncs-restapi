package shop.mtcoding.blog.domain.course.courseteacher;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "course_teacher_tb")
public class CourseTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private CourseTeacherEnum role; // 메인강사, 보조강사

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public CourseTeacher(Long id, Course course, Teacher teacher, CourseTeacherEnum role, LocalDateTime createdAt) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.role = role;
        this.createdAt = createdAt;
    }
}
