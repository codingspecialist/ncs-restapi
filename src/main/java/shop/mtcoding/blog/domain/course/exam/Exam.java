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

    // TODO: 시험을 강제로 만드는 생성자와 시험을 쳤을때 만드는 생성자 2개 팩토리로 만들 필요가 있음!!

    @Enumerated(EnumType.STRING)
    private ExamResultStatus resultStatus; // 통과, 미통과(60점미만), 결석, 미응시
    private Double totalScore; // 시험결과 점수 (재평가라면 10% 감점)
    private Double totalScorePercent; // 감점 후 백분율까지된 점수
    private Boolean isScoreFinalized; // (true 채점완료, false 채점안됨)

    private Integer gradeLevel; // 시험결과 수준

    private Boolean isActive; // default true 사용유무 (본평가를 쳤는데, 재평가를 치게 되면 본평가는 false)

    private String copiedPaperType; // 본평가,재평가
    private Double copiedTotalPoint; // 만점 점수
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
    public Exam(Long id, Student student, Teacher teacher, Subject subject, Paper paper, ExamResultStatus resultState, Double resultScore, Double percentScore, Integer grade, Boolean isUse, String paperTypeCopy, Double totalPointCopy, String questionTypeCopy, String studentSign, LocalDateTime studentSignUpdatedAt, String teacherComment, LocalDateTime commentUpdatedAt, String submitLink, Boolean gradingComplete, LocalDateTime createdAt) {
        this.id = id;
        this.student = student;
        this.teacher = teacher;
        this.subject = subject;
        this.paper = paper;
        this.resultState = resultState;
        this.resultScore = resultScore;
        this.percentScore = percentScore;
        this.grade = grade;
        this.isUse = isUse;
        this.paperTypeCopy = paperTypeCopy;
        this.totalPointCopy = totalPointCopy;
        this.questionTypeCopy = questionTypeCopy;
        this.studentSign = studentSign;
        this.studentSignUpdatedAt = studentSignUpdatedAt;
        this.teacherComment = teacherComment;
        this.commentUpdatedAt = commentUpdatedAt;
        this.submitLink = submitLink;
        this.gradingComplete = gradingComplete;
        this.createdAt = createdAt;
    }

    public void updateSign(String studentSign) {
        this.studentSign = studentSign;
        this.studentSignUpdatedAt = LocalDateTime.now();
    }

    public void updateTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
        this.commentUpdatedAt = LocalDateTime.now(); // 총평 남겼다는 인증 시간
    }

    public void updatePointAndGrade(Double resultScore) {
        this.resultScore = resultScore;
        this.percentScore = MyUtil.scaleTo100(resultScore, paper.getTotalPoint());

        if (percentScore >= 90) {
            grade = 5;
        } else if (percentScore >= 80) {
            grade = 4;
        } else if (percentScore >= 70) {
            grade = 3;
        } else if (percentScore >= 60) {
            grade = 2;
        } else {
            grade = 1;
        }

        if (grade > 1) {
            resultState = ExamResultStatus.PASS;
        } else {
            resultState = ExamResultStatus.FAIL;
        }
        gradingComplete = true;
    }

    public void setNotUse() {
        this.isUse = false;
    }

    // 결석, 미응시
    public static Exam createBlankExam(Student student, Paper paper, ExamResultStatus resultState) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacher(paper.getSubject().getTeacher())
                .teacherComment(resultState.toKorean())
                .resultScore(0.0)
                .resultState(resultState)
                .isUse(true)
                .grade(1)
                .gradingComplete(true) // 채점완료
                .percentScore(0.0)
                .build();
    }

    public QuestionType getQuestionType() {
        if (QuestionType.valueOf(questionTypeCopy) == QuestionType.MCQ) {
            return QuestionType.MCQ;
        } else {
            return QuestionType.RUBRIC;
        }
    }

}
