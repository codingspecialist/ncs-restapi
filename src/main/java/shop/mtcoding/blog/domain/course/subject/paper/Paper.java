package shop.mtcoding.blog.domain.course.subject.paper;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 시험지는 순수해야 한다. 연관관계를 맺지 말자.
 * 2. 시험이 연관관계를 가져야 한다.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "paper_tb")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String evaluationWay; // 평가방법 (서술형, 혼합형, 포트폴리오) enum 변경필요
    private LocalDate evaluationDate; // 평가일

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    private Integer count; // 문항수
    private String paperState; // 본평가, 재평가
    private Boolean isReEvaluation;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    public void addExam(Exam exam) {
        this.exams.add(exam);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }


    @Builder
    public Paper(Long id, String evaluationWay, LocalDate evaluationDate, Subject subject, Integer count, String paperState, Boolean isReEvaluation, LocalDateTime createdAt) {
        this.id = id;
        this.evaluationWay = evaluationWay;
        this.evaluationDate = evaluationDate;
        this.subject = subject;
        this.count = count;
        this.paperState = paperState;
        this.isReEvaluation = paperState.equals("재평가");
        this.createdAt = createdAt;
    }
}
