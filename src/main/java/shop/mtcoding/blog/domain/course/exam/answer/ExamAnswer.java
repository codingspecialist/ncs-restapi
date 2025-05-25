package shop.mtcoding.blog.domain.course.exam.answer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

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
    private Boolean isCorrect; // true이면 맞춘거임

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void update(Integer selectedOptionNo, Boolean isCorrect) {
        this.selectedOptionNo = selectedOptionNo;
        this.isCorrect = isCorrect;
    }

    @Builder
    public ExamAnswer(Long id, Exam exam, Question question, Integer questionNo, Integer selectedOptionNo, Boolean isCorrect, LocalDateTime createdAt) {
        this.id = id;
        this.exam = exam;
        this.question = question;
        this.questionNo = questionNo;
        this.selectedOptionNo = selectedOptionNo;
        this.isCorrect = isCorrect;
        this.createdAt = createdAt;
    }
}
