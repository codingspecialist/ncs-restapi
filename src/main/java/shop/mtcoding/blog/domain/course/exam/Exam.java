package shop.mtcoding.blog.domain.course.exam;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.exam.result.ExamResult;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionType;
import shop.mtcoding.blog.domain.course.subject.paper.question.mcq.QuestionMcqOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.rubric.QuestionRubricOption;
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
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    @Enumerated(EnumType.STRING)
    private ExamResultStatus resultStatus;

    @Enumerated(EnumType.STRING)
    private ExamNotTakenReason notTakenReason;
    private Double rawScore;
    private Double totalScore;
    private Double totalScorePercent;
    private Integer gradeLevel;
    private Boolean isActive;

    private String copiedPaperType;
    private Double copiedMaxScore;
    private String copiedQuestionType;

    @Lob
    private String studentSign;
    private LocalDateTime studentSignedAt;

    private String teacherComment;
    private LocalDateTime teacherCommentedAt;

    private String rubricSubmitLink;

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
    public Exam(Long id, Student student, Teacher teacher, Subject subject, Paper paper, ExamResultStatus resultStatus, ExamNotTakenReason notTakenReason, Double rawScore, Double totalScore, Double totalScorePercent, Integer gradeLevel, Boolean isActive, String copiedPaperType, Double copiedMaxScore, String copiedQuestionType, String studentSign, LocalDateTime studentSignedAt, String teacherComment, LocalDateTime teacherCommentedAt, String rubricSubmitLink, LocalDateTime createdAt) {
        this.id = id;
        this.student = student;
        this.teacher = teacher;
        this.subject = subject;
        this.paper = paper;
        this.resultStatus = resultStatus;
        this.notTakenReason = notTakenReason;
        this.rawScore = rawScore;
        this.totalScore = totalScore;
        this.totalScorePercent = totalScorePercent;
        this.gradeLevel = gradeLevel;
        this.isActive = isActive;
        this.copiedPaperType = copiedPaperType;
        this.copiedMaxScore = copiedMaxScore;
        this.copiedQuestionType = copiedQuestionType;
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
                .copiedPaperType(paper.getPaperType().toString())
                .copiedQuestionType(paper.getQuestionType().toString())
                .copiedMaxScore(paper.getMaxScore())
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

    public QuestionType getQuestionType() {
        return QuestionType.valueOf(copiedQuestionType);
    }

    // -------------------------
    // 🔒 내부 로직은 private 처리
    // -------------------------

    private void gradeMcq() {
        validateQuestionType(QuestionType.MCQ);

        double total = examAnswers.stream()
                .mapToDouble(answer -> {
                    double score = calculateMcqScore(answer);
                    answer.setExamResult(buildResult(answer, score, null));
                    return score;
                })
                .sum();

        finalizeGrading(total);
    }

    private void gradeRubric(Map<Long, String> feedbackPRLinkMap) {
        validateQuestionType(QuestionType.RUBRIC);

        double total = examAnswers.stream()
                .mapToDouble(answer -> {
                    double score = calculateRubricScore(answer, answer.getSelectedOptionNo());
                    String feedbackPRLink = feedbackPRLinkMap.get(answer.getId());
                    answer.setExamResult(buildResult(answer, score, feedbackPRLink));
                    return score;
                })
                .sum();

        finalizeGrading(total);
    }

    private void validateQuestionType(QuestionType expected) {
        if (getQuestionType() != expected) {
            throw new IllegalStateException(expected + " 시험만 가능합니다.");
        }
    }

    private Double calculateMcqScore(ExamAnswer answer) {
        return answer.getQuestion().getMcqOptions().stream()
                .filter(opt -> opt.getNo().equals(answer.getSelectedOptionNo()))
                .mapToDouble(QuestionMcqOption::getPoint)
                .findFirst()
                .orElse(0);
    }

    private Double calculateRubricScore(ExamAnswer answer, Integer selectedNo) {
        return answer.getQuestion().getRubricOptions().stream()
                .filter(opt -> opt.getNo().equals(selectedNo))
                .mapToDouble(QuestionRubricOption::getPoint)
                .findFirst()
                .orElse(0);
    }

    private ExamResult buildResult(ExamAnswer answer, Double scoredPoint, String codeReviewFeedbackPRLink) {
        return ExamResult.builder()
                .examAnswer(answer)
                .scoredPoint(scoredPoint)
                .isCorrect(scoredPoint > 0)
                .codeReviewFeedbackPRLink(codeReviewFeedbackPRLink)
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
