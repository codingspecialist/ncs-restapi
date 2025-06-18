package shop.mtcoding.blog.domain.course.subject.paper.question;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;

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
    private String stimulusImg; // (객관식 보기 지문)
    private String scenario; // (루브릭 시나리오 지문 = 전체 노션 상세 예시에 있지만 중복되어 들어간다.)

    @ManyToOne(fetch = FetchType.LAZY)
    private SubjectElement subjectElement;

    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<QuestionOption> questionOptions = new ArrayList<>();

    public void addOption(QuestionOption option) {
        questionOptions.add(option);
    }

    @Builder
    public Question(Long id, Integer no, String title, String stimulusImg, String scenario,
                    SubjectElement subjectElement, Paper paper, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.title = title;
        this.stimulusImg = stimulusImg;
        this.scenario = scenario;
        this.subjectElement = subjectElement;
        this.paper = paper;
        this.createdAt = createdAt;
    }
}
