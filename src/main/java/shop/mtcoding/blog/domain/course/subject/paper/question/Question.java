package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.errors.exception.api.Exception500;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.mcq.McqOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.rubric.RubricOption;

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
    private QuestionType questionType; // Enum(MCQ, RUBRIC)

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<RubricOption> rubricOptions = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<McqOption> mcqOptions = new ArrayList<>();

    @Builder
    public Question(Long id, Integer no, String title, String exContent, String exScenario, SubjectElement subjectElement, Paper paper, LocalDateTime createdAt, QuestionType questionType) {
        this.id = id;
        this.no = no;
        this.title = title;
        this.exContent = exContent;
        this.exScenario = exScenario;
        this.subjectElement = subjectElement;
        this.paper = paper;
        this.createdAt = createdAt;
        this.questionType = questionType;
    }

    public void addRubric(RubricOption rubric) {
        this.rubricOptions.add(rubric);
        rubric.setQuestion(this); // 연관관계의 주인 쪽도 세팅
    }

    public void addMcq(McqOption mcq) {
        this.mcqOptions.add(mcq);
        mcq.setQuestion(this); // 연관관계의 주인 쪽도 세팅
    }

    public List<?> getOptions() {
        if (this.questionType == QuestionType.MCQ) {
            return mcqOptions;
        } else if (this.questionType == QuestionType.RUBRIC) {
            return rubricOptions;
        } else {
            throw new Exception500("Unknown question type: " + questionType);
        }
    }
}
