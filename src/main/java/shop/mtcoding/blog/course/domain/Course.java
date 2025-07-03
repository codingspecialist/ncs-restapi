package shop.mtcoding.blog.course.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.domain.enums.CourseStatus;
import shop.mtcoding.blog.course.domain.enums.CourseTeacherEnum;
import shop.mtcoding.blog.user.domain.Student;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 크로스플랫폼 과정에 특정 회차 정보
 * 2. 새로운 코스를 등록할 때 해당 Course 정보를 불러오기 할 수 있다.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "course_tb")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK
    private String code; // 과정ID (국가에서 쓰는 것) 한과정이 5번 실행되면, 과정아이디 같다.
    private String title; // 과정명
    private Integer level; // 훈련수준
    private Integer round; // 1회차, 2회차
    private String purpose; // 과정목표
    private Integer totalTime; // 과정시간
    private Integer totalDay; // 과정일수
    private LocalDate startDate; // 년월일
    private LocalDate endDate; // 년월일
    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus; // 과정진행전, 과정진행중, 과정종료 (기본값은 과정진행전이다 - 숫자로는 0번)

    // JPA의 cascade 동작 기준은 FK 주인이 아니라, 어그리게이트 루트 객체가 persist() 되는지를 기준으로 합니다.
    // Course가 이미 영속 상태, 혹은 비영속 Course가 persist될때 함께 insert 된다.
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseTeacher> courseTeachers = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> subjects = new ArrayList<>();


    public void addCourseTeacher(CourseTeacher ct) {
        if (!courseTeachers.contains(ct)) {
            courseTeachers.add(ct);
            ct.setCourse(this);
        }
    }

    public void addSubject(Subject subject) {
        if (!subjects.contains(subject)) {
            // 루트에서 교과목 담기
            subjects.add(subject);

            // 교과목에서도 루트 담아주기 (담지 않으면 Subject가 course_id를 관리하는데, fk가 null이 됨)
            subject.setCourse(this);
        }
    }


    @CreationTimestamp
    private LocalDateTime createdAt;


    @Builder
    public Course(Long id, String title, String code, Integer level, String purpose, Integer totalTime, Integer totalDay, Integer round, LocalDate startDate, LocalDate endDate, CourseStatus courseStatus, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.code = code;
        this.level = level;
        this.purpose = purpose;
        this.totalTime = totalTime;
        this.totalDay = totalDay;
        this.round = round;
        this.startDate = startDate;
        this.endDate = endDate;
        this.courseStatus = courseStatus;
        this.createdAt = createdAt;
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getMainTeacherName() {
        return courseTeachers.stream()
                .filter(ct -> ct.getRole() == CourseTeacherEnum.MAIN)
                .map(ct -> ct.getTeacher().getName())
                .findFirst()
                .orElse(null); // 혹은 null 처리 가능
    }
}

