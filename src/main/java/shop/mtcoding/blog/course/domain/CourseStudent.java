package shop.mtcoding.blog.course.domain;

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

    //MSA는 같은 도메인끼리는 ORM 객체로 하고 다른 도메인은 fk 변수로 관리한다
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
