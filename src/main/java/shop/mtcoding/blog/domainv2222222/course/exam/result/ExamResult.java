package shop.mtcoding.blog.domainv2222222.course.exam.result;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domainv2222222.course.exam.answer.ExamAnswer;

import java.time.LocalDateTime;

// 학생이 제출한 시험의 답변들에 대한 결과
@NoArgsConstructor
@Getter
@Entity
@Table(name = "exam_result_tb")
public class ExamResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private ExamAnswer examAnswer;

    private Double scoredPoint; // 5점, 4점, 3점, 2점, 1점 (머든지 될 수 있음)
    private Boolean isCorrect; // 0점이 아니면 전부다 true임!! 루브릭은 모든 답변에 점수가 부여됨
    private String codeReviewFeedbackPrLink; // (선택적)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public ExamResult(Long id, ExamAnswer examAnswer, Double scoredPoint, Boolean isCorrect, String codeReviewFeedbackPrLink, LocalDateTime createdAt) {
        this.id = id;
        this.examAnswer = examAnswer;
        this.scoredPoint = scoredPoint;
        this.isCorrect = isCorrect;
        this.codeReviewFeedbackPrLink = codeReviewFeedbackPrLink;
        this.createdAt = createdAt;
    }
}
