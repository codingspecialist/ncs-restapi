package shop.mtcoding.blog.domain.course.exam.answer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.exam.result.ExamResult;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.time.LocalDateTime;

// 학생이 제출한 시험의 답변들
@NoArgsConstructor
@Getter
@Entity
@Table(name = "exam_answer_tb")
public class ExamAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Exam exam; // 시험지 1개 - 시험은 25개(학생수만큼)

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question; // 시험지 1개 - 시험문제 20개(문제수만큼)

    private Integer questionNo;

    // 객관식은 자동 계산 / 그게 아니면 수동 계산됨
    private Integer selectedOptionNo;

    private String codeReviewRequestLink; // (선택적)

    @OneToOne(mappedBy = "examAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private ExamResult examResult;

    public void setExamResult(ExamResult examResult) {
        this.examResult = examResult;
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public ExamAnswer(Long id, Exam exam, Question question, Integer questionNo, Integer selectedOptionNo, String codeReviewRequestLink, LocalDateTime createdAt) {
        this.id = id;
        this.exam = exam;
        this.question = question;
        this.questionNo = questionNo;
        this.selectedOptionNo = selectedOptionNo;
        this.codeReviewRequestLink = codeReviewRequestLink;
        this.createdAt = createdAt;
    }

    public static ExamAnswer createMcqAnswer(Exam exam, Question question, Integer questionNo, Integer selectedOptionNo) {
        return ExamAnswer.builder()
                .exam(exam)
                .question(question)
                .questionNo(questionNo)
                .selectedOptionNo(selectedOptionNo)
                .build();
    }

    public static ExamAnswer createRubricAnswer(Exam exam, Question question, Integer questionNo, String codeReviewRequestLink) {
        return ExamAnswer.builder()
                .exam(exam)
                .question(question)
                .questionNo(questionNo)
                .codeReviewRequestLink(codeReviewRequestLink)
                .build();
    }

    public Boolean getIsRight() {
        return examResult != null ? examResult.getIsCorrect() : null;
    }
}
