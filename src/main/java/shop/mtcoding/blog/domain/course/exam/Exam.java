package shop.mtcoding.blog.domain.course.exam;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.exam.result.ExamResult;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionOption;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.web.exam.ExamRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "exam_tb")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private ExamResultStatus resultStatus;

    @Enumerated(EnumType.STRING)
    private ExamNotTakenReason notTakenReason;
    private Double rawScore; // 시험 원점수 ex) 13점
    private Double totalScore; // 정답 맞춘 점수 재평가시 0.9 곱하기 ex) 13*0.9점
    private Double totalScorePercent; // totalScore 100점 만점으로 환산
    private Integer gradeLevel; // 수준 1~5
    private Boolean isActive; // 해당 paper의 paperStage가 재평가가 생기면, 본평가는 isActive false

    private String copiedPaperVersion; // copied가 붙은것은 다른 테이블에 있는데 비정규화로 중복해서 저장 (본평가, 재평가)
    private String copiedEvaluationWay; // MCQ, PROJECT, PRACTICE
    private Double copiedMaxScore; // 만점 점수 ex) 15점

    @Lob
    private String studentSign;
    private LocalDateTime studentSignedAt;

    private String teacherComment;
    private LocalDateTime teacherCommentedAt;

    private String rubricSubmitLink; //

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamAnswer> examAnswers = new ArrayList<>();

    public void addAnswer(ExamAnswer answer) {
        this.examAnswers.add(answer);
    }

    public List<ExamAnswer> getExamAnswers() {
        return Collections.unmodifiableList(this.examAnswers);
    }

    @Builder
    public Exam(Long id, Subject subject, Paper paper, Student student, Teacher teacher, ExamResultStatus resultStatus, ExamNotTakenReason notTakenReason, Double rawScore, Double totalScore, Double totalScorePercent, Integer gradeLevel, Boolean isActive, String copiedPaperVersion, String copiedEvaluationWay, Double copiedMaxScore, String studentSign, LocalDateTime studentSignedAt, String teacherComment, LocalDateTime teacherCommentedAt, String rubricSubmitLink, LocalDateTime createdAt) {
        this.id = id;
        this.subject = subject;
        this.paper = paper;
        this.student = student;
        this.teacher = teacher;
        this.resultStatus = resultStatus;
        this.notTakenReason = notTakenReason;
        this.rawScore = rawScore;
        this.totalScore = totalScore;
        this.totalScorePercent = totalScorePercent;
        this.gradeLevel = gradeLevel;
        this.isActive = isActive;
        this.copiedPaperVersion = copiedPaperVersion;
        this.copiedEvaluationWay = copiedEvaluationWay;
        this.copiedMaxScore = copiedMaxScore;
        this.studentSign = studentSign;
        this.studentSignedAt = studentSignedAt;
        this.teacherComment = teacherComment;
        this.teacherCommentedAt = teacherCommentedAt;
        this.rubricSubmitLink = rubricSubmitLink;
        this.createdAt = createdAt;
    }


    private static ExamBuilder baseBuilder(Student student, Paper paper) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacher(paper.getSubject().getTeacher())
                .copiedPaperVersion(paper.getPaperVersion().toString())
                .copiedMaxScore(paper.getMaxScore())
                .copiedEvaluationWay(paper.getEvaluationWay().toString())
                .isActive(true);
    }

    public static Exam createNotTakenExamWithReason(Student student, Paper paper, ExamNotTakenReason notTakenReason) {
        return baseBuilder(student, paper)
                .resultStatus(ExamResultStatus.NOT_TAKEN)
                .notTakenReason(notTakenReason)
                .teacherComment(notTakenReason.toKorean())
                .build();
    }

    public static Exam createNotTakenExam(Student student, Paper paper) {
        return baseBuilder(student, paper)
                .resultStatus(ExamResultStatus.NOT_TAKEN)
                .teacherComment(ExamResultStatus.NOT_TAKEN.toKorean())
                .student(student)
                .build();
    }

    public static Exam createMcqExam(Student student, Paper paper) {
        return baseBuilder(student, paper)
                .resultStatus(ExamResultStatus.NOT_GRADED)
                .build();
    }

    public static Exam createRubricExam(Student student, Paper paper, String rubricSubmitLink) {
        return baseBuilder(student, paper)
                .rubricSubmitLink(rubricSubmitLink)
                .resultStatus(ExamResultStatus.NOT_GRADED)
                .build();
    }

    public void applyMcqGrading(List<ExamRequest.GradeMcq.AnswerMcq> answers, String teacherComment) {
        updateTeacherComment(teacherComment);

        for (ExamRequest.GradeMcq.AnswerMcq dto : answers) {
            ExamAnswer answer = this.examAnswers.stream()
                    .filter(a -> a.getId().equals(dto.getAnswerId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("answerId " + dto.getAnswerId() + " not found"));

            answer.setSelectedOptionNo(dto.getSelectedOptionNo());
        }

        gradeMcq();
    }

    public void applyRubricGrading(List<ExamRequest.GradeRubric.AnswerRubric> answers, String teacherComment) {
        updateTeacherComment(teacherComment);

        Map<Long, String> prLinkMap = answers.stream()
                .collect(Collectors.toMap(
                        ExamRequest.GradeRubric.AnswerRubric::getAnswerId,
                        ExamRequest.GradeRubric.AnswerRubric::getCodeReviewFeedbackPRLink));

        for (ExamRequest.GradeRubric.AnswerRubric dto : answers) {
            ExamAnswer answer = this.examAnswers.stream()
                    .filter(a -> a.getId().equals(dto.getAnswerId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("answerId " + dto.getAnswerId() + " not found"));

            answer.setSelectedOptionNo(dto.getSelectedOptionNo());
        }

        gradeRubric(prLinkMap);
    }

    public void updateStudentSign(String studentSign) {
        this.studentSign = studentSign;
        this.studentSignedAt = LocalDateTime.now();
    }

    public void updateTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
        this.teacherCommentedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
    }

    public EvaluationWay getEvaluationWay() {
        return EvaluationWay.valueOf(copiedEvaluationWay);
    }

    // -------------------------
    // 🔒 내부 로직은 private 처리
    // -------------------------

    private void gradeMcq() {

        double total = examAnswers.stream()
                .mapToDouble(answer -> {
                    double score = calculateScore(answer);
                    answer.setExamResult(buildResult(answer, score, null));
                    return score;
                })
                .sum();

        finalizeGrading(total);
    }

    private void gradeRubric(Map<Long, String> feedbackPRLinkMap) {

        double total = examAnswers.stream()
                .mapToDouble(answer -> {
                    double score = calculateScore(answer);
                    String feedbackPRLink = feedbackPRLinkMap.get(answer.getId());
                    answer.setExamResult(buildResult(answer, score, feedbackPRLink));
                    return score;
                })
                .sum();

        finalizeGrading(total);
    }

    private Double calculateScore(ExamAnswer answer) {
        return answer.getQuestion().getQuestionOptions().stream()
                .filter(opt -> opt.getNo().equals(answer.getSelectedOptionNo()))
                .mapToDouble(QuestionOption::getPoint)
                .findFirst()
                .orElse(0);
    }

    private ExamResult buildResult(ExamAnswer answer, Double scoredPoint, String codeReviewFeedbackPrLink) {
        return ExamResult.builder()
                .examAnswer(answer)
                .scoredPoint(scoredPoint)
                .isCorrect(scoredPoint > 0)
                .codeReviewFeedbackPrLink(codeReviewFeedbackPrLink)
                .build();
    }

    private void finalizeGrading(Double sumPoints) {
        this.rawScore = sumPoints;
        this.totalScore = rawScore;

        if (paper.isReTest()) {
            this.totalScore = rawScore * subject.getScorePolicy();
        }

        this.totalScorePercent = MyUtil.scaleTo100(totalScore, copiedMaxScore);

        this.gradeLevel = switch ((int) (totalScorePercent / 10)) {
            case 9, 10 -> 5;
            case 8 -> 4;
            case 7 -> 3;
            case 6 -> 2;
            default -> 1;
        };

        this.resultStatus = (gradeLevel > 1) ? ExamResultStatus.PASS : ExamResultStatus.FAIL;
    }
}
