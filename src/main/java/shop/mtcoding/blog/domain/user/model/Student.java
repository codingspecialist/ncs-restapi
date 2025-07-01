package shop.mtcoding.blog.domain.user.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.model.Course;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private String name;
    private String birthday; // 생년월일 (800825)
    private LocalDate dropOutDate; // 중탈 날짜
    private String dropOutReason; // 중탈 이유
    private String comment; // 학생 모든 교과목에 대한 총평
    private Integer gradeLevel; // 학생 모든 교과목에 대한 수준 1,2,3,4,5
    @Enumerated(EnumType.STRING)
    private StudentStatus studentStatus; // 취업, 중도탈락, 미이수, 이수, 재학중

    @Column(unique = true)
    private String authCode; // 학생 인증 코드
    private Boolean isVerified; // 학생 인증 여부

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setUser(User user) {
        this.user = user;
    }

    public void setVerified() {
        this.isVerified = true;
        this.authCode = null;
    }

    @Builder
    public Student(Long id, Course course, String name, String birthday, LocalDate dropOutDate, String dropOutReason, String comment, Integer gradeLevel, StudentStatus studentStatus, String authCode, Boolean isVerified, LocalDateTime createdAt, User user) {
        this.id = id;
        this.course = course;
        this.name = name;
        this.birthday = birthday;
        this.dropOutDate = dropOutDate;
        this.dropOutReason = dropOutReason;
        this.comment = comment;
        this.gradeLevel = gradeLevel;
        this.studentStatus = studentStatus;
        this.authCode = authCode;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
        this.user = user;
    }
}
