package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.errors.exception.api.Exception500;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.mcq.QuestionMcqOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.rubric.QuestionRubricOption;

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

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionRubricOption> rubricOptions = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionMcqOption> mcqOptions = new ArrayList<>();

    @Builder
    public Question(Long id, Integer no, String title, String exContent, String exScenario, SubjectElement subjectElement, Paper paper, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.title = title;
        this.exContent = exContent;
        this.exScenario = exScenario;
        this.subjectElement = subjectElement;
        this.paper = paper;
        this.createdAt = createdAt;
    }

    public void addRubric(QuestionRubricOption rubric) {
        this.rubricOptions.add(rubric);
        rubric.setQuestion(this); // 연관관계의 주인 쪽도 세팅
    }

    public void addMcq(QuestionMcqOption mcq) {
        this.mcqOptions.add(mcq);
        mcq.setQuestion(this); // 연관관계의 주인 쪽도 세팅
    }

    public List<?> getOptions() {
        if (paper.getQuestionType() == QuestionType.MCQ) {
            return mcqOptions;
        } else if (paper.getQuestionType() == QuestionType.RUBRIC) {
            return rubricOptions;
        } else {
            throw new Exception500("Unknown question type: " + paper.getQuestionType());
        }
    }
}
