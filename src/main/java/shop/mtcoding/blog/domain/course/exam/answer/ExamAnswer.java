package shop.mtcoding.blog.domain.course.exam.answer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;

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
    private Integer selectedOptionNo;

    private Integer earnedPoint; // 5점, 4점, 3점, 2점, 1점 (머든지 될 수 있음) - 이건 배점이 아님!!
    private Boolean isRight; // 객관식에만 사용

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void autoGrade() {
        // 1. 선택한 번호에 해당하는 옵션 찾기
        QuestionOption selectedOption = question.getQuestionOptions()
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


    /**
     * 강사 수동 입력용
     */
    public void manualGrade(int earnedPoint) {
        this.isRight = null; // 의미 없음
        this.earnedPoint = earnedPoint;
    }

    public void update(Integer selectedOptionNo, Boolean isRight) {
        this.selectedOptionNo = selectedOptionNo;
        this.isRight = isRight;
    }

    @Builder
    public ExamAnswer(Long id, Exam exam, Question question, Integer questionNo, Integer selectedOptionNo, Integer earnedPoint, Boolean isRight, LocalDateTime createdAt) {
        this.id = id;
        this.exam = exam;
        this.question = question;
        this.questionNo = questionNo;
        this.selectedOptionNo = selectedOptionNo;
        this.earnedPoint = earnedPoint;
        this.isRight = isRight;
        this.createdAt = createdAt;
    }
}
