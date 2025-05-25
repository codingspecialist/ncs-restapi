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

/**
 * 1. 교과목 정보 (자바, 스프링)
 * 2. 교과목 세부 수업 내용은 SubjectUnit 기입
 * 3. 해당 교과목은 2회차 시행시에 불러오기 해서 특정 정보만 변경해서 저장할 수 있다.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "subject_tb")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code; // 교과목 ID (능력단위코드)
    private String title; // 교과목명 (능력단위명)
    private String purpose; // 교과목 목표
    private String gubun; // NCS, 비NCS
    private Integer grade; // 교과목 수준
    private Integer totalTime; // 교과목 시간
    private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 순번
    private String learningWay; // 교수 학습 방법

    private LocalDate startDate; // 교과목 시작일
    private LocalDate endDate; // 교과목 종료일
    private String teacherName; // 교과목 훈련교사 이름 (CourseTeacher 조인해서 교과목에서 담당 선생님 확인해야함)

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

    // 코스에 회차 정보가 있기 때문에 특정 회차에 대한 교과목이다. 다음 회차에서 적기 귀찮으면 불러오기 해서 저장할 수 있다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Subject(Long id, String code, String title, String purpose, String gubun, Integer grade, Integer totalTime, Integer no, String learningWay, LocalDate startDate, LocalDate endDate, String teacherName, Course course, LocalDateTime createdAt) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.purpose = purpose;
        this.gubun = gubun;
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
