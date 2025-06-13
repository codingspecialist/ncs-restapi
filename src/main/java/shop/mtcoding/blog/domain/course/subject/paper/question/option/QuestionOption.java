package shop.mtcoding.blog.domain.course.subject.paper.question.option;

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
@Table(name = "question_option_tb",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"no", "question_id"})})
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer no; // 시험문제의 4지선답 번호
    private String content; // 아이컨텍/인사/말하기 or 사지선답 항목

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question; // 발표력 or 문제제목 (Question쪽에 타입 필요할듯)

    private Boolean isRight; // true 이면 정답

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public QuestionOption(Long id, Integer no, String content, Question question, Boolean isRight, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.content = content;
        this.question = question;
        this.isRight = isRight;
        this.createdAt = createdAt;
    }
}
