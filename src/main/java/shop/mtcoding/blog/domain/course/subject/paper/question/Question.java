package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.errors.exception.api.Exception500;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.mcq.QuestionMcq;
import shop.mtcoding.blog.domain.course.subject.paper.question.rubric.QuestionRubric;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "question_tb")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer no; // 시험지에 종속되는 시험 순서 1, 2, 3
    private String title; // 시험 문제 제목
    private String exContent; // (선택) (객관식 보기 지문)
    private String exScenario; // (선택) (루브릭 보기 지문)

    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectElement subjectElement; // 여기 내부에 평가기준 있음!  criterion

    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private QuestionType type; // Enum(MCQ, RUBRIC)

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionRubric> rubrics = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionMcq> mcqs = new ArrayList<>();

    @Builder
    public Question(Long id, Integer no, String title, String exContent, String exScenario, SubjectElement subjectElement, Paper paper, LocalDateTime createdAt, QuestionType type) {
        this.id = id;
        this.no = no;
        this.title = title;
        this.exContent = exContent;
        this.exScenario = exScenario;
        this.subjectElement = subjectElement;
        this.paper = paper;
        this.createdAt = createdAt;
        this.type = type;
    }

    public void addRubric(QuestionRubric rubric) {
        this.rubrics.add(rubric);
        rubric.setQuestion(this); // 연관관계의 주인 쪽도 세팅
    }

    public void addMcq(QuestionMcq mcq) {
        this.mcqs.add(mcq);
        mcq.setQuestion(this); // 연관관계의 주인 쪽도 세팅
    }

    public List<?> getOptions() {
        if (this.type == QuestionType.MCQ) {
            return mcqs;
        } else if (this.type == QuestionType.RUBRIC) {
            return rubrics;
        } else {
            throw new Exception500("Unknown question type: " + type);
        }
    }
}
