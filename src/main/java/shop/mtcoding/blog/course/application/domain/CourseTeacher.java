package shop.mtcoding.blog.course.application.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.application.domain.enums.TeacherType;
import shop.mtcoding.blog.user.application.domain.Teacher;

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

    // TODO: MSA 전환시 FK id참조로 전환
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private TeacherType teacherType; // 메인강사, 보조강사

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public CourseTeacher(Long id, Course course, Teacher teacher, TeacherType teacherType, LocalDateTime createdAt) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.teacherType = teacherType;
        this.createdAt = createdAt;
    }

    public static CourseTeacher create(Course course, Teacher teacher, TeacherType teacherType) {
        return CourseTeacher.builder()
                .course(course)
                .teacher(teacher)
                .teacherType(teacherType)
                .build();
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
