package shop.mtcoding.blog.domain.course.exam.answer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.mcq.QuestionMcqOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.rubric.QuestionRubricOption;

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


    private String codeReviewRequestLink;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder


    // 시스템이 자동 채점
    public void autoMcqGrade() {
        // 1. 선택한 번호에 해당하는 옵션 찾기
        QuestionMcqOption selectedOption = question.getMcqOptions()
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
        QuestionRubricOption selectedOption = question.getRubricOptions()
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
