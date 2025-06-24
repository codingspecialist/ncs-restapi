package shop.mtcoding.blog.domain.course.subject;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "subject_tb")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    private Integer no; // 과정 내 순번
    private String code; // 교과목 ID (능력단위코드)
    private String title; // 교과목명 (능력단위명)
    private String purpose; // 교과목 목표

    @Enumerated(EnumType.STRING)
    private NcsType ncsType; // NCS, 비NCS

    private Integer grade; // 교과목 수준
    private Integer totalTime; // 교과목 시간

    @Enumerated(EnumType.STRING)
    private LearningWay learningWay; // 교수 학습 방법

    private Double scorePolicy; // 결시자/재평가자 감점 비율 (0.9, 0.8)

    private LocalDate startDate;
    private LocalDate endDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<SubjectElement> elements = new ArrayList<>();

    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
    private List<Paper> papers = new ArrayList<>();

    public void addPaper(Paper paper) {
        this.papers.add(paper);
    }

    public void addElement(SubjectElement element) {
        this.elements.add(element);
    }

    @Builder
    public Subject(Long id, Teacher teacher, Course course, Integer no, String code, String title, String purpose, NcsType ncsType, Integer grade, Integer totalTime, LearningWay learningWay, Double scorePolicy, LocalDate startDate, LocalDate endDate, LocalDateTime createdAt) {
        this.id = id;
        this.teacher = teacher;
        this.course = course;
        this.no = no;
        this.code = code;
        this.title = title;
        this.purpose = purpose;
        this.ncsType = ncsType;
        this.grade = grade;
        this.totalTime = totalTime;
        this.learningWay = learningWay;
        this.scorePolicy = scorePolicy;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdAt = createdAt;
    }
}
