package shop.mtcoding.blog.course.application.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.user.domain.Student;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "course_student_tb")
public class CourseStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    // TODO: MSA 전환시 FK id참조로 전환
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public CourseStudent(Long id, Course course, Student student, LocalDateTime createdAt) {
        this.id = id;
        this.course = course;
        this.student = student;
        this.createdAt = createdAt;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
