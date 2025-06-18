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

    private String evaluationRoom;     // 평가 장소
    private String evaluationDevice;   // 평가 장비 정보

    // -------------------- 객관식이 아닐때 받아야 할 목록
    private String pblTitle;
    private String pblScenario;
    private String pblScenarioGuideLink;
    private String pblSubmitFormat; // 제출항목 (notion)
    private String pblSubmitTemplateLink; // 제출항목 복제 템플릿 (선택)
    private String pblChallenge; // 도전과제

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
    public Paper(Long id, EvaluationWay evaluationWay, LocalDate evaluationDate, Subject subject, PaperType paperType, String evaluationRoom, String evaluationDevice, String pblTitle, String pblScenario, String pblScenarioGuideLink, String pblSubmitFormat, String pblSubmitTemplateLink, String pblChallenge, LocalDateTime createdAt) {
        this.id = id;
        this.evaluationWay = evaluationWay;
        this.evaluationDate = evaluationDate;
        this.subject = subject;
        this.paperType = paperType;
        this.evaluationRoom = evaluationRoom;
        this.evaluationDevice = evaluationDevice;
        this.pblTitle = pblTitle;
        this.pblScenario = pblScenario;
        this.pblScenarioGuideLink = pblScenarioGuideLink;
        this.pblSubmitFormat = pblSubmitFormat;
        this.pblSubmitTemplateLink = pblSubmitTemplateLink;
        this.pblChallenge = pblChallenge;
        this.createdAt = createdAt;
    }
}
