package shop.mtcoding.blog.course.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.exam.domain.Paper;
import shop.mtcoding.blog.subject.domain.Subject;
import shop.mtcoding.blog.subject.domain.SubjectElement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "course_subject_tb") // 새로운 테이블명
public class CourseSubject { // CourseSubject로 이름 변경

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course; // 어떤 과정에 소속된 교과목인지

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject; // 어떤 (공통)교과목인지

    // 해당 과정-교과목의 담당 선생님.
    // CourseTeacher를 참조하는 것이 더 적절합니다.
    @ManyToOne(fetch = FetchType.LAZY)
    private CourseTeacher courseTeacher;

    private Integer no; // 과정 내 순번
    private Integer totalTime; // 이 과정에서 이 교과목의 총 시간
    private LocalDate startDate;
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // SubjectElement와 Paper도 이제 CourseSubject에 1:N 관계로 연결됩니다.
    // (특정 과정의 특정 교과목에 대한 세부 요소 및 시험지)
    @OneToMany(mappedBy = "courseSubject", cascade = CascadeType.ALL) // mappedBy 필드 이름 변경 필요
    private List<SubjectElement> elements = new ArrayList<>();

    @OneToMany(mappedBy = "courseSubject", cascade = CascadeType.ALL) // mappedBy 필드 이름 변경 필요
    private List<Paper> papers = new ArrayList<>();

    public void addPaper(Paper paper) {
        this.papers.add(paper);
    }

    public void addElement(SubjectElement element) {
        this.elements.add(element);
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Builder
    public CourseSubject(Long id, Course course, Subject subject, CourseTeacher courseTeacher, Integer no, Integer totalTime, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt) {
        this.id = id;
        this.course = course;
        this.subject = subject;
        this.courseTeacher = courseTeacher;
        this.no = no;
        this.totalTime = totalTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }
}
