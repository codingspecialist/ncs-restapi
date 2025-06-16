package shop.mtcoding.blog.domain.course.subject.paper;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.domain.course.exam.Exam;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "paper_tb")
public class Paper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EvaluationWay evaluationWay; // 평가 방법

    private LocalDate evaluationDate; // 평가일

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private PaperType paperType; // 본평가, 재평가

    @Lob
    private String guideSummary; // 훈련생에게 보여줄 요약 가이드
    private String guideLink; // 강사용 노션/GitHub 등 외부 링크
    private String evaluationRoom;     // 평가 장소
    private String evaluationDevice;   // 평가 장비 정보
    private Double scorePolicy;        // 평가 배점 정책
    private String submissionFormat;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    public void addExam(Exam exam) {
        this.exams.add(exam);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public boolean isReEvaluation() {
        return paperType.isReEvaluation();
    }

    @Builder
    public Paper(Long id, EvaluationWay evaluationWay, LocalDate evaluationDate, Subject subject, PaperType paperType, String guideSummary, String guideLink, String evaluationRoom, String evaluationDevice, Double scorePolicy, String submissionFormat, LocalDateTime createdAt) {
        this.id = id;
        this.evaluationWay = evaluationWay;
        this.evaluationDate = evaluationDate;
        this.subject = subject;
        this.paperType = paperType;
        this.guideSummary = guideSummary;
        this.guideLink = guideLink;
        this.evaluationRoom = evaluationRoom;
        this.evaluationDevice = evaluationDevice;
        this.scorePolicy = scorePolicy;
        this.submissionFormat = submissionFormat;
        this.createdAt = createdAt;
    }
}
