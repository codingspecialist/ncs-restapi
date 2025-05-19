package shop.mtcoding.blog.user.student;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

// INFO: 학생은 과정에 종속된다
@NoArgsConstructor
@Getter
@Entity
@Table(name = "student_tb")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private String birthday; // 생년월일 (800825)
    private LocalDate dropOutDate; // 중탈 날짜
    private String dropOutReason; // 중탈 이유
    private String comment; // 학생 모든 교과목에 대한 총평
    private Integer grade; // 학생 모든 교과목에 대한 수준 1,2,3,4,5
    @Enumerated(EnumType.STRING)
    private StudentEnum state; // 취업, 중도탈락, 미이수, 이수, 재학중
    private String authCode; // 학생 인증 코드
    private Boolean isVerified; // 학생 인증 여부
    private Integer studentNo; // 교과목 1에 학생번호 1 (유니크 복합키)
    @CreationTimestamp
    private LocalDateTime createDate;

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    // 학생이 입력될 때 마다 자동으로 이름순으로 번호 업데이트
    public void updateStudentNo(Integer studentNo) {
        this.studentNo = studentNo;
    }

    @Builder
    public Student(Long id, User user, Course course, String birthday, LocalDate dropOutDate, String dropOutReason, String comment, Integer grade, StudentEnum state, String authCode, Boolean isVerified, Integer studentNo, LocalDateTime createDate) {
        this.id = id;
        this.user = user;
        this.course = course;
        this.birthday = birthday;
        this.dropOutDate = dropOutDate;
        this.dropOutReason = dropOutReason;
        this.comment = comment;
        this.grade = grade;
        this.state = state;
        this.authCode = authCode;
        this.isVerified = isVerified;
        this.studentNo = studentNo;
        this.createDate = createDate;
    }
}
