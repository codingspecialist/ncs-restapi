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
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.web.exam.ExamRequest;

import java.time.LocalDateTime;
import java.util.*;
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
    public Exam(Long id, Student student, Teacher teacher, Subject subject, Paper paper, ExamResultStatus resultStatus, Double totalScore, Double totalScorePercent, Integer gradeLevel, Boolean isActive, String copiedPaperType, Double copiedMaxScore, String copiedQuestionType, String studentSign, LocalDateTime studentSignedAt, String teacherComment, LocalDateTime teacherCommentedAt, String rubricSubmitLink, LocalDateTime createdAt) {
        this.id = id;
        this.student = student;
        this.teacher = teacher;
        this.subject = subject;
        this.paper = paper;
        this.resultStatus = resultStatus;
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

    public static Exam createAbsentExam(Student student, Paper paper) {
        return baseBuilder(student, paper)
                .resultStatus(ExamResultStatus.ABSENT)
                .teacherComment(ExamResultStatus.ABSENT.toKorean())
                .build();
    }

    public static Exam createNotTakenExam(Student student, Paper paper) {
        return baseBuilder(student, paper)
                .resultStatus(ExamResultStatus.NOT_TAKEN)
                .teacherComment(ExamResultStatus.NOT_TAKEN.toKorean())
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

    public void gradeMcq() {
        validateQuestionType(QuestionType.MCQ);

        double total = examAnswers.stream()
                .mapToDouble(answer -> {
                    int score = calculateMcqScore(answer);
                    answer.setExamResult(buildResult(answer, score, null));
                    return score;
                })
                .sum();

        finalizeGrading(total);
    }

    public void gradeRubric() {
        validateQuestionType(QuestionType.RUBRIC);

        double total = examAnswers.stream()
                .mapToDouble(answer -> {
                    int score = calculateRubricScore(answer, answer.getSelectedOptionNo());
                    answer.setExamResult(buildResult(answer, score, null));
                    return score;
                })
                .sum();

        finalizeGrading(total);
    }

    public void regradeRubric(ExamRequest.GradeDTO dto) {
        validateQuestionType(QuestionType.RUBRIC);

        Map<Long, ExamAnswer> answerMap = this.examAnswers.stream()
                .collect(Collectors.toMap(ExamAnswer::getId, a -> a));

        double totalScore = dto.getAnswerGrades().stream()
                .mapToDouble(grade -> {
                    ExamAnswer answer = Optional.ofNullable(answerMap.get(Long.valueOf(grade.getAnswerId())))
                            .orElseThrow(() -> new RuntimeException("Invalid answerId: " + grade.getAnswerId()));

                    int score = calculateRubricScore(answer, grade.getSelectedOptionNo());
                    answer.getExamResult().update(score, score > 0, grade.getCodeReviewPRLink());
                    return score;
                }).sum();

        finalizeGrading(totalScore);
    }

    public void regradeMcq(ExamRequest.GradeDTO dto) {
        validateQuestionType(QuestionType.MCQ);

        Map<Long, ExamAnswer> answerMap = this.examAnswers.stream()
                .collect(Collectors.toMap(ExamAnswer::getId, a -> a));

        double totalScore = dto.getAnswerGrades().stream()
                .mapToDouble(grade -> {
                    ExamAnswer answer = Optional.ofNullable(answerMap.get(Long.valueOf(grade.getAnswerId())))
                            .orElseThrow(() -> new RuntimeException("Invalid answerId: " + grade.getAnswerId()));

                    int score = calculateMcqScore(answer);
                    answer.getExamResult().update(score, score > 0, null);
                    return score;
                }).sum();

        finalizeGrading(totalScore);
    }

    private void validateQuestionType(QuestionType expected) {
        if (getQuestionType() != expected) {
            throw new IllegalStateException(expected + " 시험만 가능합니다.");
        }
    }

    private int calculateMcqScore(ExamAnswer answer) {
        return answer.getQuestion().getMcqOptions().stream()
                .filter(opt -> opt.getNo().equals(answer.getSelectedOptionNo()))
                .mapToInt(opt -> opt.getPoint())
                .findFirst().orElse(0);
    }

    private int calculateRubricScore(ExamAnswer answer, Integer selectedNo) {
        return answer.getQuestion().getRubricOptions().stream()
                .filter(opt -> opt.getNo().equals(selectedNo))
                .mapToInt(opt -> opt.getPoint())
                .findFirst().orElse(0);
    }

    private ExamResult buildResult(ExamAnswer answer, Integer score, String prLink) {
        return ExamResult.builder()
                .examAnswer(answer)
                .scoredPoint(score)
                .isCorrect(score > 0)
                .codeReviewFeedbackPRLink(prLink)
                .build();
    }

    private void finalizeGrading(Double sumPoints) {
        this.totalScore = sumPoints;
        this.totalScorePercent = MyUtil.scaleTo100(sumPoints, copiedMaxScore);

        this.gradeLevel = switch ((int) (totalScorePercent / 10)) {
            case 9, 10 -> 5;
            case 8 -> 4;
            case 7 -> 3;
            case 6 -> 2;
            default -> 1;
        };

        this.resultStatus = (gradeLevel > 1) ? ExamResultStatus.PASS : ExamResultStatus.FAIL;
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
}
