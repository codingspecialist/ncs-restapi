package shop.mtcoding.blog.course.student;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "student_tb")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private String name;
    private String birthday; // 생년월일 (800825)
    private LocalDate dropOutDate; // 중탈 날짜
    private String dropOutReason; // 중탈 이유
    private String comment; // 학생 모든 교과목에 대한 총평
    private Integer grade; // 학생 모든 교과목에 대한 수준 1,2,3,4,5
    @Enumerated(EnumType.STRING)
    private StudentEnum state; // 취업, 중도탈락, 미이수, 이수, 재학중

    @Column(unique = true)
    private String authCode; // 학생 인증 코드
    private Boolean isVerified; // 학생 인증 여부
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Boolean checkNameAndBirthday(String name, String birthday) {
        if (this.name == name && this.birthday == birthday) return true;
        else return false;
    }

    public void setVerified(User user) {
        this.isVerified = true;
        this.authCode = null;
        this.user = user;
    }

    @Builder
    public Student(Long id, User user, String name, Course course, String birthday, LocalDate dropOutDate, String dropOutReason, String comment, Integer grade, StudentEnum state, String authCode, Boolean isVerified, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.course = course;
        this.birthday = birthday;
        this.dropOutDate = dropOutDate;
        this.dropOutReason = dropOutReason;
        this.comment = comment;
        this.grade = grade;
        this.state = state;
        this.authCode = authCode;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
    }
}
