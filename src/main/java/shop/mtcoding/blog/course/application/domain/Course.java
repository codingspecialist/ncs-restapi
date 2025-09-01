package shop.mtcoding.blog.course.application.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.application.domain.enums.CourseStatus;
import shop.mtcoding.blog.course.application.domain.enums.TeacherType;
import shop.mtcoding.blog.course.web.dto.CourseRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Aggregate root
// 과정등록 -> 과정 선생님 등록 -> 교과목 등록
// 시험 -> 시험지등록, 문제등록, 문제옵션등록
// 나중에 입학한 첫날 -> 학생들에게 회원가입하세요 (이메일 가서 인증코드 확인하세요)
// 과정에 학생등록 (이 부분은 추후에도 할 수 있어야 한다)
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
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseTeacher> courseTeachers = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseStudent> courseStudents = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> subjects = new ArrayList<>();

    public void addCourseTeacher(CourseTeacher teacher) {
        if (!courseTeachers.contains(teacher)) {
            courseTeachers.add(teacher);
            teacher.setCourse(this);
        }
    }

    public void addCourseStudent(CourseStudent student) {
        if (!courseStudents.contains(student)) {
            courseStudents.add(student);
            student.setCourse(this);
        }
    }

    public void addCourseSubject(Subject subject) {
        if (!subjects.contains(subject)) {
            // 루트에서 교과목 담기
            subjects.add(subject);
            subject.setCourse(this);
        }
    }

    @Builder
    public Course(Long id, String title, String code, Integer level, String purpose, Integer totalTime,
            Integer totalDay, Integer round, LocalDate startDate, LocalDate endDate, CourseStatus courseStatus,
            LocalDateTime createdAt) {
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

    public static Course create(CourseRequest.Save request) {
        CourseStatus initialStatus = MyUtil.courseStatusUpdate(request.startDate(), request.endDate());

        return Course.builder()
                .code(request.code())
                .title(request.title())
                .level(request.level())
                .round(request.round())
                .purpose(request.purpose())
                .totalTime(request.totalTime())
                .totalDay(request.totalDay())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .courseStatus(initialStatus)
                .build();
    }

    public void setCourseStatus(CourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getMainTeacherName() {
        return courseTeachers.stream()
                .filter(ct -> ct.getTeacherType() == TeacherType.MAIN)
                .map(ct -> ct.getTeacher().getName())
                .findFirst()
                .orElse(null); // 혹은 null 처리 가능
    }
}
