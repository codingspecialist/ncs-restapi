package shop.mtcoding.blog.domain.course.exam;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;

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
    private String teacherName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    // 시험지
    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    // 학생이 같은 과목에 본평가만 볼 수 있다 (통과)
    // 학생이 같은 과목에 재평가만 볼 수 있다 (결석)
    // 학생이 같은 과목에 본평가와, 재평가를 볼 수 있다 (본평가 점수 60점 미만 = 미통과)
    // 점수 통계낼때나, 보여줄때 재평가가 있으면 재평가로 보여줘야 한다.
    // 재평가는 통과할 때까지 다시 친다.

    private String examState; // 본평가, 재평가 (paperType)
    private String reExamReason; // 결석 or 미통과(60점미만) - 재평가이유

    private String passState; // 통과, 미통과, 결석, 미응시

    private Double score; // 시험결과 점수 (재평가라면 10% 감점)
    private Double finalScore; // 감점 후 백분율까지된 점수
    private Integer grade; // 시험결과 수준

    // default : true
    private Boolean isUse; // 사용유무 (본평가를 쳤는데, 재평가를 치게 되면 본평가는 false로 변경됨)

    @Lob
    private String studentSign;
    private LocalDateTime studentSignUpdatedAt;

    private String teacherComment;
    private LocalDateTime commentUpdatedAt;

    // ------------- 객관식이 아닐때 받아야함
    private String submitLink;
    private Boolean standby; // 채점중 (false, true)

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamAnswer> examAnswers = new ArrayList<>();

    public void addAnswer(ExamAnswer answer) {
        this.examAnswers.add(answer);
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void updateSign(String studentSign) {
        this.studentSign = studentSign;
        this.studentSignUpdatedAt = LocalDateTime.now();
    }

    public void updateTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
        this.commentUpdatedAt = LocalDateTime.now(); // 총평 남겼다는 인증 시간
    }

    public void updatePointAndGrade(Double score, Double sumQuestionPoints) {
        this.score = score;

        this.finalScore = MyUtil.scaleTo100(score, sumQuestionPoints);

        if (finalScore >= 90) {
            grade = 5;
        } else if (finalScore >= 80) {
            grade = 4;
        } else if (finalScore >= 70) {
            grade = 3;
        } else if (finalScore >= 60) {
            grade = 2;
        } else {
            grade = 1;
        }

        if (grade > 1) {
            passState = "통과";
            reExamReason = "";
        } else {
            passState = "미통과";
            reExamReason = "60점미만";
        }
        standby = true;
    }

    public void setNotUse() {
        this.isUse = false;
    }

    public static Exam createAbsentExam(Student student, Paper paper) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacherName(paper.getSubject().getTeacherName())
                .examState(paper.getPaperType().toKorean()) // 본평가 or 재평가
                .reExamReason("결석")
                .teacherComment("결석")
                .score(0.0)
                .passState("미통과")
                .isUse(true)
                .grade(1)
                .standby(true)
                .finalScore(0.0)
                .build();
    }

    @Builder
    public Exam(Long id, Student student, String teacherName, Subject subject, Paper paper, String examState, String reExamReason, String passState, Double score, Integer grade, Boolean isUse, String studentSign, LocalDateTime studentSignUpdatedAt, String teacherComment, LocalDateTime commentUpdatedAt, String submitLink, Boolean standby, LocalDateTime createdAt, Double finalScore) {
        this.id = id;
        this.student = student;
        this.teacherName = teacherName;
        this.subject = subject;
        this.paper = paper;
        this.examState = examState;
        this.reExamReason = reExamReason;
        this.passState = passState;
        this.score = score;
        this.grade = grade;
        this.isUse = isUse;
        this.studentSign = studentSign;
        this.studentSignUpdatedAt = studentSignUpdatedAt;
        this.teacherComment = teacherComment;
        this.commentUpdatedAt = commentUpdatedAt;
        this.submitLink = submitLink;
        this.standby = standby;
        this.createdAt = createdAt;
        this.finalScore = finalScore;
    }
}
