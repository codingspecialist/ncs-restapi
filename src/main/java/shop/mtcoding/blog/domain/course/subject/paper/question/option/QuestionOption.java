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
    // TODO: rubric_test
    private String rubricItem; // 0점일때는 없고, 점수가 있을때만 존재하는 부분

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question; // 발표력 or 문제제목 (Question쪽에 타입 필요할듯)

    // TODO: rubric_test
    private Integer point; // 정답이면 점수, 정답 아니면 0점 // 루브릭일때는 모든 옵션에 점수가 있음.
    private Boolean isRight; // 점수가 0점이 아니면 다 true

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public QuestionOption(Long id, Integer no, String content, String rubricItem, Question question, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.content = content;
        this.rubricItem = rubricItem;
        this.question = question;
        this.point = point;
        this.isRight = point > 0;
        this.createdAt = createdAt;
    }
}
