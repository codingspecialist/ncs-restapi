package shop.mtcoding.blog.domain.course.subject;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;

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

    private LocalDate startDate;
    private LocalDate endDate;
    private String teacherName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

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
    public Subject(Long id, String code, String title, String purpose,
                   NcsType ncsType, Integer grade, Integer totalTime, Integer no,
                   LearningWay learningWay,
                   LocalDate startDate, LocalDate endDate, String teacherName,
                   Course course, LocalDateTime createdAt) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.purpose = purpose;
        this.ncsType = ncsType;
        this.grade = grade;
        this.totalTime = totalTime;
        this.no = no;
        this.learningWay = learningWay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacherName = teacherName;
        this.course = course;
        this.createdAt = createdAt;
    }
}
