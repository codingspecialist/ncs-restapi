package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.errors.exception.api.Exception500;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "question_tb")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectElement subjectElement; // 여기 내부에 평가기준 있음!  criterion

    private Integer no; // 시험지에 종속되는 시험 순서 1, 2, 3
    private String title; // 시험 문제 제목
    private String summary; // 객관식 보기 지문, 문제 시나리오 요약

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionOption> questionOptions = new ArrayList<>();

    @Builder
    public Question(Long id, Paper paper, SubjectElement subjectElement, Integer no, String title, String summary, LocalDateTime createdAt) {
        this.id = id;
        this.paper = paper;
        this.subjectElement = subjectElement;
        this.no = no;
        this.title = title;
        this.summary = summary;
        this.createdAt = createdAt;
    }

    public void addOption(QuestionOption option) {
        this.questionOptions.add(option);
    }

    public QuestionOption getCorrectOption() {
        return this.getQuestionOptions().stream()
                .max(Comparator.comparingInt(QuestionOption::getPoint))
                .orElse(null);
    }

    public EvaluationWay getEvaluationWay() {
        if (this.paper == null) {
            throw new Exception500("Paper가 연결되어 있지 않습니다.");
        }
        return this.paper.getEvaluationWay();
    }
}
