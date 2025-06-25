package shop.mtcoding.blog.domain.course.exam.answer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.mcq.McqOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.rubric.RubricOption;

import java.time.LocalDateTime;

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
    private Integer earnedPoint; // 5점, 4점, 3점, 2점, 1점 (머든지 될 수 있음) - 이건 배점이 아님!!
    private Boolean isRight; // 0점이 아니면 전부다 true임!! 루브릭은 모든 답변에 점수가 부여됨

    private String codeReviewLink; // 선택적 (링크 들어가서 파일에 주석달고, 그거 캡쳐후 새로운 브랜치 임의생성후 PR요청)
    private String codeReviewPRLink;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public ExamAnswer(Long id, Exam exam, Question question, Integer questionNo, Integer selectedOptionNo, Integer earnedPoint, Boolean isRight, String codeReviewLink, LocalDateTime createdAt, String codeReviewPRLink) {
        this.id = id;
        this.exam = exam;
        this.question = question;
        this.questionNo = questionNo;
        this.selectedOptionNo = selectedOptionNo;
        this.earnedPoint = earnedPoint;
        this.isRight = isRight;
        this.codeReviewLink = codeReviewLink;
        this.createdAt = createdAt;
        this.codeReviewPRLink = codeReviewPRLink;
    }

    // 시스템이 자동 채점
    public void autoMcqGrade() {
        // 1. 선택한 번호에 해당하는 옵션 찾기
        McqOption selectedOption = question.getMcqOptions()
                .stream()
                .filter(option -> option.getNo().equals(this.selectedOptionNo))
                .findFirst()
                .orElse(null);

        // 2. 해당 옵션이 없을 경우
        if (selectedOption == null) {
            this.isRight = false;
            this.earnedPoint = 0;
            return;
        }

        // 3. 점수 계산: 선택한 보기의 점수
        this.earnedPoint = selectedOption.getPoint();
        this.isRight = selectedOption.getPoint() > 0; // 0점이면 오답으로 처리
    }

    // 강사가 수동 채점
    public void manualRubricGrade(String codeReviewPRLink) {
        // 1. 선택한 번호에 해당하는 옵션 찾기
        RubricOption selectedOption = question.getRubricOptions()
                .stream()
                .filter(option -> option.getNo().equals(this.selectedOptionNo))
                .findFirst()
                .orElse(null);

        // 2. 해당 옵션이 없을 경우
        if (selectedOption == null) {
            this.isRight = false;
            this.earnedPoint = 0;
            return;
        }

        // 3. 점수 계산: 선택한 보기의 점수
        this.codeReviewPRLink = codeReviewPRLink;
        this.earnedPoint = selectedOption.getPoint();
        this.isRight = selectedOption.getPoint() > 0; // 0점이면 오답으로 처리
    }
}
