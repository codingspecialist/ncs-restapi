package shop.mtcoding.blog.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 20)
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserType role; // 학생, 강사, 직원, 팀장, 원장

    @OneToOne
    private Student student;

    @OneToOne
    private Teacher teacher;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @Builder
    public User(Long id, String username, String password, String email, UserType role, LocalDateTime createdAt, Student student, Teacher teacher) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.student = student;
        this.teacher = teacher;
    }
}
