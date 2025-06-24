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
    private Integer totalPoint; // 총점 (문제가 만들어질때 마다 update 된다)

    // -------------------- 객관식이 아닐때 받아야 할 목록
    private String taskTitle;
    private String taskScenario;
    private String taskScenarioGuideLink;
    private String taskSubmitFormat;
    private String taskSubmitTemplateLink;
    private String taskChallenge;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "paper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Exam> exams = new ArrayList<>();

    // TODO : 반대방향 setter 만들기
    public void addExam(Exam exam) {
        this.exams.add(exam);
    }

    // TODO : 반대방향 setter 만들기
    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public boolean isReTest() {
        return paperType.isReTest();
    }

    // 시험지 총점
    public void updateTotalPoint(int point) {
        this.totalPoint += point;
    }

    @Builder
    public Paper(Long id, EvaluationWay evaluationWay, LocalDate evaluationDate, Subject subject, PaperType paperType, String evaluationRoom, String evaluationDevice, Integer totalPoint, String taskTitle, String taskScenario, String taskScenarioGuideLink, String taskSubmitFormat, String taskSubmitTemplateLink, String taskChallenge, LocalDateTime createdAt) {
        this.id = id;
        this.evaluationWay = evaluationWay;
        this.evaluationDate = evaluationDate;
        this.subject = subject;
        this.paperType = paperType;
        this.evaluationRoom = evaluationRoom;
        this.evaluationDevice = evaluationDevice;
        this.totalPoint = totalPoint;
        this.taskTitle = taskTitle;
        this.taskScenario = taskScenario;
        this.taskScenarioGuideLink = taskScenarioGuideLink;
        this.taskSubmitFormat = taskSubmitFormat;
        this.taskSubmitTemplateLink = taskSubmitTemplateLink;
        this.taskChallenge = taskChallenge;
        this.createdAt = createdAt;
    }
}
