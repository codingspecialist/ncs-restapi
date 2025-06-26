package shop.mtcoding.blog.domain.course.subject.paper.question.rubric;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "question_rubric_tb",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"no", "question_id"})})
public class QuestionRubricOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private Integer no; // 루브릭 번호
    private String content; // 루브릭 내용
    private Integer point; // 루브릭 점수

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public QuestionRubricOption(Long id, Integer no, String content, Question question, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.content = content;
        this.question = question;
        this.point = point;
        this.createdAt = createdAt;
    }

    public Integer getPointBySelectedOptionNo(int selectedOptionNo) {
        if (selectedOptionNo == this.no) {
            return point;
        } else {
            return 0;
        }
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
