package shop.mtcoding.blog.domain.course.subject.paper.question.mcq;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.time.LocalDateTime;

/**
 * 시험문제의 옵션 4가지 항목
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "question_mcq_tb",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"no", "question_id"})})
public class QuestionMcq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private Integer no; // 4지선답 번호
    private String content; // 4지선답 항목
    private Integer point;
    private Boolean isRight;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public QuestionMcq(Long id, Integer no, String content, Question question, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.content = content;
        this.question = question;
        this.point = point;
        this.isRight = point > 0;
        this.createdAt = createdAt;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
