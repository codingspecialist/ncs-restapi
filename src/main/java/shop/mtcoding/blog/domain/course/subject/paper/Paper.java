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

@NoArgsConstructor
@Getter
@Entity
@Table(name = "paper_tb")
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EvaluationWay evaluationWay; // 평가 방법

    private LocalDate evaluationDate; // 평가일

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    private Integer questionCount; // 문항 수

    @Enumerated(EnumType.STRING)
    private PaperType paperType; // 본평가, 재평가

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

    public boolean isReEvaluation() {
        return paperType.isReEvaluation();
    }

    @Builder
    public Paper(Long id, EvaluationWay evaluationWay, LocalDate evaluationDate,
                 Subject subject, Integer questionCount, PaperType paperType, LocalDateTime createdAt) {
        this.id = id;
        this.evaluationWay = evaluationWay;
        this.evaluationDate = evaluationDate;
        this.subject = subject;
        this.questionCount = questionCount;
        this.paperType = paperType;
        this.createdAt = createdAt;
    }
}
