package shop.mtcoding.blog.course.exam;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.exam.answer.ExamAnswer;
import shop.mtcoding.blog.course.student.Student;
import shop.mtcoding.blog.paper.Paper;

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

    // 시험지
    @ManyToOne(fetch = FetchType.LAZY)
    private Paper paper;

    // 학생이 같은 과목에 본평가만 볼 수 있다 (통과)
    // 학생이 같은 과목에 재평가만 볼 수 있다 (결석)
    // 학생이 같은 과목에 본평가와, 재평가를 볼 수 있다 (본평가 점수 60점 미만 = 미통과)
    // 점수 통계낼때나, 보여줄때 재평가가 있으면 재평가로 보여줘야 한다.
    // 재평가는 통과할 때까지 다시 친다.

    private String examState; // 본평가, 재평가
    private String reExamReason; // 결석 or 미통과(60점미만) - 재평가이유

    private String passState; // 통과, 미통과, 결석

    private Double score; // 시험결과 점수 (재평가라면 10% 감점)
    private Integer grade; // 시험결과 수준

    // default : true
    private Boolean isUse; // 사용유무 (본평가를 쳤는데, 재평가를 치게 되면 본평가는 false로 변경됨)

    @Lob
    private String studentSign;
    private LocalDateTime studentSignUpdatedAt;

    private String teacherComment;
    private LocalDateTime commentUpdatedAt;

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

    public void updatePointAndGrade(Double score) {
        this.score = score;
        if (score >= 90) {
            grade = 5;
        } else if (score >= 80) {
            grade = 4;
        } else if (score >= 70) {
            grade = 3;
        } else if (score >= 60) {
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
    }

    public void setNotUse() {
        this.isUse = false;
    }

    @Builder
    public Exam(Boolean isUse, Long id, Student student, String teacherName, Paper paper, String examState, String reExamReason, String passState, Double score, Integer grade, String studentSign, String teacherComment, LocalDateTime commentUpdatedAt, LocalDateTime createdAt) {
        this.id = id;
        this.student = student;
        this.teacherName = teacherName;
        this.paper = paper;
        this.examState = examState;
        this.reExamReason = reExamReason;
        this.passState = passState;
        this.score = score;
        this.grade = grade;
        this.studentSign = studentSign;
        this.teacherComment = teacherComment;
        this.commentUpdatedAt = commentUpdatedAt;
        this.createdAt = createdAt;
        this.isUse = isUse;
    }
}
