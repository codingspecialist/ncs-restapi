package shop.mtcoding.blog.paper.question;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.subject.element.SubjectElement;
import shop.mtcoding.blog.paper.Paper;
import shop.mtcoding.blog.paper.question.option.QuestionOption;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 시험지에 종속된 문제
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "question_tb")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer no; // 시험지에 종속되는 시험 순서 1, 2, 3
    private String title; // 시험 문제 제목
    private Integer point; // 시험 문제 점수 10점, 20점
    private Integer answerNumber; // 4번이 정담

    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectElement subjectElement;

    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QuestionOption> questionOptions = new ArrayList<>();

    public void addOption(QuestionOption option){
        questionOptions.add(option);
    }

    @Builder
    public Question(Long id, Integer no, String title, Integer point, Integer answerNumber, Paper paper, LocalDateTime createDate, SubjectElement subjectElement) {
        this.id = id;
        this.no = no;
        this.title = title;
        this.point = point;
        this.answerNumber = answerNumber;
        this.paper = paper;
        this.createDate = createDate;
        this.subjectElement = subjectElement;
    }
}