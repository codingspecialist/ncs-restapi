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

    private String resultState; // 통과, 미통과(60점미만), 결석, 미응시
    private Double resultScore; // 시험결과 점수 (재평가라면 10% 감점)
    private Double percentScore; // 감점 후 백분율까지된 점수
    private Integer grade; // 시험결과 수준
    private Boolean isUse; // default true 사용유무 (본평가를 쳤는데, 재평가를 치게 되면 본평가는 false)

    private String paperTypeCopy; // 본평가,재평가
    private Double totalPointCopy; // 만점 점수

    @Lob
    private String studentSign;
    private LocalDateTime studentSignUpdatedAt;

    private String teacherComment;
    private LocalDateTime commentUpdatedAt;

    // ------------- 객관식이 아닐때 받아야함
    private String submitLink;
    private Boolean gradingComplete; // (true 채점완료, false 채점안됨)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private List<ExamAnswer> examAnswers = new ArrayList<>();

    // TODO : 반대방향 setter 필요
    public void addAnswer(ExamAnswer answer) {
        this.examAnswers.add(answer);
    }

    @Builder
    public Exam(Long id, Student student, Teacher teacher, Subject subject, Paper paper, String resultState, Double resultScore, Double percentScore, Integer grade, Boolean isUse, String paperTypeCopy, Double totalPointCopy, String studentSign, LocalDateTime studentSignUpdatedAt, String teacherComment, LocalDateTime commentUpdatedAt, String submitLink, Boolean gradingComplete, LocalDateTime createdAt) {
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
            resultState = "통과";
        } else {
            resultState = "미통과(60점미만)";
        }
        gradingComplete = true;
    }

    public void setNotUse() {
        this.isUse = false;
    }

    // 결석, 미응시
    public static Exam createBlankExam(Student student, Paper paper, String reason) {
        return Exam.builder()
                .student(student)
                .paper(paper)
                .subject(paper.getSubject())
                .teacher(paper.getSubject().getTeacher())
                .teacherComment(reason)
                .resultScore(0.0)
                .resultState(reason)
                .isUse(true)
                .grade(1)
                .gradingComplete(true) // 채점완료
                .percentScore(0.0)
                .build();
    }

}
