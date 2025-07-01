package shop.mtcoding.blog.domain.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.model.Course;
import shop.mtcoding.blog.domain.user.application.dto.UserCommand;

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

    @OneToOne
    private Emp emp;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public User(Long id, String username, String password, String email, UserType role, Student student, Teacher teacher, Emp emp, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.student = student;
        this.teacher = teacher;
        this.emp = emp;
        this.createdAt = createdAt;
    }


    public static User from(UserCommand.TeacherJoin command) {
        // 1. Teacher 엔티티 생성
        Teacher teacher = Teacher.builder()
                .name(command.name())
                .sign(command.sign())
                .build();

        // 2. User 엔티티 생성 및 Teacher 연결
        User user = User.builder()
                .username(command.username())
                .password(command.password())
                .email(command.email())
                .role(command.role())
                .teacher(teacher) // 여기서 Teacher와 User 연결
                .build();

        // 3. 양방향 연관 관계 설정 (필요시)
        teacher.setUser(user);
        return user;
    }

    public static User from(UserCommand.StudentJoin command, Course course, String authCode) {
        Student student = Student.builder()
                .studentStatus(StudentStatus.ENROLL)
                .course(course)
                .birthday(command.birthday())
                .authCode(authCode)
                .isVerified(false)
                .name(command.name())
                .build();

        User user = User.builder()
                .username(command.username())
                .password(command.password())
                .email(command.email())
                .role(command.role())
                .student(student)
                .build();

        student.setUser(user);
        return user;
    }

    public static User from(UserCommand.EmpJoin command) {
        Emp emp = Emp.builder()
                .name(command.name())
                .sign(command.sign())
                .build();

        User user = User.builder()
                .username(command.username())
                .password(command.password())
                .email(command.email())
                .role(command.role())
                .emp(emp)
                .build();

        emp.setUser(user);
        return user;
    }
}
