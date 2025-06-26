package shop.mtcoding.blog.domain.course.exam;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionType;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 실행된 시험
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "exam_tb")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시험 보는 학생
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    // 시험 담당 강사
    @ManyToOne(fetch = FetchType.LAZY)
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    // 시험지
    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper; // paperType은 여기서 확인

    @Enumerated(EnumType.STRING)
    private ExamResultStatus resultStatus; // 통과, 미통과(60점미만), 결석, 미응시
    private Double totalScore; // 시험결과 점수 (재평가라면 10% 감점)
    private Double totalScorePercent; // 감점 후 백분율까지된 점수

    private Integer gradeLevel; // 시험결과 수준
    private Boolean isActive; // default true 사용유무 (본평가를 쳤는데, 재평가를 치게 되면 본평가는 false)

    private String copiedPaperType; // 본평가,재평가
    private Double copiedMaxScore; // 만점 점수
    private String copiedQuestionType; // 객관식 or 루브릭

    @Lob
    private String studentSign;
    private LocalDateTime studentSignedAt;

    private String teacherComment;
    private LocalDateTime teacherCommentedAt;

    // ------------- 객관식이 아닐때 받아야함
    private String rubricSubmitLink;


    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamAnswer> examAnswers = new ArrayList<>();

    // TODO : 반대방향 setter 필요
    public void addAnswer(ExamAnswer answer) {
        this.examAnswers.add(answer);
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

    public static Exam createMcqExam() {
        return null;
    }

    // 채점전
    public static Exam createRubricExamIsNotGraded(Student student, Paper paper, String rubricSubmitLink) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacher(paper.getSubject().getTeacher())
                .copiedPaperType(paper.getPaperType().toKorean()) // ORIGINAL vs RETEST
                .copiedQuestionType(paper.getQuestionType().toString()) // MCQ vs RUBRIC
                .copiedMaxScore(paper.getMaxScore())
                .rubricSubmitLink(rubricSubmitLink)
                .resultStatus(ExamResultStatus.NOT_GRADED)
                .isActive(true)
                .gradeLevel(null)
                .teacherComment(null)
                .totalScore(null)
                .totalScorePercent(null)
                .studentSign(null)
                .studentSignedAt(null)
                .teacherCommentedAt(null)
                .build();
    }

    // 결석
    public static Exam createAbsentExam(Student student, Paper paper) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacher(paper.getSubject().getTeacher())
                .teacherComment(ExamResultStatus.ABSENT.toKorean())
                .resultStatus(ExamResultStatus.ABSENT)
                .copiedPaperType(paper.getPaperType().toKorean()) // ORIGINAL vs RETEST
                .copiedQuestionType(paper.getQuestionType().toString()) // MCQ vs RUBRIC
                .copiedMaxScore(paper.getMaxScore())
                .isActive(true)
                .gradeLevel(null)
                .totalScore(null)
                .totalScorePercent(null)
                .studentSign(null)
                .studentSignedAt(null)
                .teacherCommentedAt(null)
                .rubricSubmitLink(null)
                .build();
    }

    // 미응시
    public static Exam createNotTakenExam(Student student, Paper paper) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacher(paper.getSubject().getTeacher())
                .teacherComment(ExamResultStatus.NOT_TAKEN.toKorean())
                .resultStatus(ExamResultStatus.NOT_TAKEN)
                .copiedPaperType(paper.getPaperType().toKorean()) // ORIGINAL vs RETEST
                .copiedQuestionType(paper.getQuestionType().toString()) // MCQ vs RUBRIC
                .copiedMaxScore(paper.getMaxScore())
                .isActive(true)
                .gradeLevel(null)
                .totalScore(null)
                .totalScorePercent(null)
                .studentSign(null)
                .studentSignedAt(null)
                .teacherCommentedAt(null)
                .rubricSubmitLink(null)
                .build();
    }


    public void updateSign(String studentSign) {
        this.studentSign = studentSign;
        this.studentSignedAt = LocalDateTime.now();
    }

    public void updateTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
        this.teacherCommentedAt = LocalDateTime.now(); // 총평 남겼다는 인증 시간
    }

    public void updatePointAndGrade(Double resultScore) {
        this.totalScore = resultScore;
        this.totalScorePercent = MyUtil.scaleTo100(resultScore, paper.getTotalPoint());

        if (totalScorePercent >= 90) {
            gradeLevel = 5;
        } else if (totalScorePercent >= 80) {
            gradeLevel = 4;
        } else if (totalScorePercent >= 70) {
            gradeLevel = 3;
        } else if (totalScorePercent >= 60) {
            gradeLevel = 2;
        } else {
            gradeLevel = 1;
        }

        if (gradeLevel > 1) {
            resultStatus = ExamResultStatus.PASS;
        } else {
            resultStatus = ExamResultStatus.FAIL;
        }
    }

    public void setNotUse() {
        this.isActive = false;
    }


    public QuestionType getQuestionType() {
        if (QuestionType.valueOf(copiedQuestionType) == QuestionType.MCQ) {
            return QuestionType.MCQ;
        } else {
            return QuestionType.RUBRIC;
        }
    }

}
