package shop.mtcoding.blog.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.user.application.port.in.dto.UserCommand;
import shop.mtcoding.blog.user.domain.enums.StudentStatus;
import shop.mtcoding.blog.user.domain.enums.UserRole;

import java.time.LocalDateTime;

// 어그리게이트 루트
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
    private UserRole role; // 학생, 강사, 직원

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Teacher teacher;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Emp emp;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // 1. 빌더

    @Builder
    public User(Long id, String username, String password, String email, UserRole role, Student student, Teacher teacher, Emp emp, LocalDateTime createdAt) {
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

    // 2. 연관관계 편의메서드

    public void setStudent(Student student) {
        if (this.student != null) this.student.setUser(null); // 이전 연결 끊기
        this.student = student;
        if (student != null) student.setUser(this); // 새 연결 맺기
    }

    public void setTeacher(Teacher teacher) {
        if (this.teacher != null) this.teacher.setUser(null); // 이전 연결 끊기
        this.teacher = teacher;
        if (teacher != null) teacher.setUser(this); // 새 연결 맺기
    }

    public void setEmp(Emp emp) {
        if (this.emp != null) this.emp.setUser(null); // 이전 연결 끊기
        this.emp = emp;
        if (emp != null) emp.setUser(this); // 새 연결 맺기
    }

    // 3. 팩토리 메서드 from 혹은 create
    public static User createStudent(UserCommand.StudentJoin command, Course course, String authCode) {
        Student student = Student.builder()
                .studentStatus(StudentStatus.ENROLL) // 기본 상태
                .course(course) // Course 엔티티는 서비스에서 조회하여 전달
                .birthday(command.birthday())
                .authCode(authCode)
                .isVerified(false) // 기본값
                .name(command.name())
                .build();

        User user = User.builder()
                .username(command.username())
                .password(command.password())
                .email(command.email())
                .role(UserRole.STUDENT) // 역할 명시
                .student(student) // User가 주인이므로 Student 객체 설정
                .build();

        student.setUser(user); // Student 비-주인 쪽의 setUser 호출 (양방향 관계 완성)
        return user;
    }

    public static User createTeacher(UserCommand.TeacherJoin command) {
        Teacher teacher = Teacher.builder()
                .name(command.name())
                .sign(command.sign())
                .build();

        User user = User.builder()
                .username(command.username())
                .password(command.password())
                .email(command.email())
                .role(UserRole.TEACHER) // 역할 명시
                .teacher(teacher) // User가 주인이므로 Teacher 객체 설정
                .build();

        teacher.setUser(user); // Teacher 비-주인 쪽의 setUser 호출 (양방향 관계 완성)
        return user;
    }

    public static User createEmp(UserCommand.EmpJoin command) {
        Emp emp = Emp.builder()
                .name(command.name())
                .sign(command.sign())
                .build();

        User user = User.builder()
                .username(command.username())
                .password(command.password())
                .email(command.email())
                .role(UserRole.EMP) // 역할 명시
                .emp(emp) // User가 주인이므로 Emp 객체 설정
                .build();

        emp.setUser(user); // Emp 비-주인 쪽의 setUser 호출 (양방향 관계 완성)
        return user;
    }

    // 4. 도메인에 필요한 로직들

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

}
