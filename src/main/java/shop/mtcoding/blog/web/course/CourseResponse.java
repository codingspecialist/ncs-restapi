package shop.mtcoding.blog.web.course;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.user.student.Student;

import java.time.LocalDate;
import java.util.List;

public class CourseResponse {

    @Data
    public static class Item {
        private Long courseId;
        private String title;
        private String code;
        private Integer totalTime;
        private Integer totalDay;
        private Integer round;
        private LocalDate startDate;
        private LocalDate endDate;
        private String teacherName;
        private String courseStatus;

        public Item(Course course) {
            this.courseId = course.getId();
            this.title = course.getTitle();
            this.code = course.getCode();
            this.totalTime = course.getTotalTime();
            this.totalDay = course.getTotalDay();
            this.round = course.getRound();
            this.startDate = course.getStartDate();
            this.endDate = course.getEndDate();
            this.teacherName = course.getMainTeacherName();
            this.courseStatus = course.getCourseStatus().toKorean();
        }
    }

    @Data
    public static class Items {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부
        private List<Item> courses;

        public Items(Page<Course> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.courses = paging.getContent().stream().map(Item::new).toList();
        }
    }

    @Data
    public static class Detail {
        private Item course;
        private List<SubjectItem> subjects;
        private List<StudentItem> students;

        public Detail(Course course, List<Subject> subjects, List<Student> students) {
            this.course = new Item(course);
            this.subjects = subjects.stream().map(SubjectItem::new).toList();
            this.students = students.stream().map(StudentItem::new).toList();
        }

        @Data
        class StudentItem {
            private Long studentId;
            private String name; // 학생 번호는 이름순으로 해서 rownum 뽑자
            private String birthday;
            private String studentStatus; // 취업, 중도탈락, 미이수, 이수, 재학중
            private String dropOutDate; // 중탈 날짜
            private String dropOutReason; // 중탈 이유
            private String comment; // 학생 모든 교과목에 대한 총평
            private String grade; // 학생 모든 교과목에 대한 수준 1,2,3,4,5
            private String authCode; // 인증코드
            private Long courseId;

            public StudentItem(Student student) {
                this.studentId = student.getId();
                this.name = student.getName();
                this.birthday = student.getBirthday();
                this.studentStatus = student.getStudentStatus().toKorean();
                this.dropOutDate = student.getDropOutDate() == null ? "" : student.getDropOutDate().toString();
                this.dropOutReason = student.getDropOutReason() == null ? "" : student.getDropOutReason();
                this.comment = student.getComment() == null ? "" : student.getComment();
                this.grade = student.getGrade() == null ? "" : student.getGrade().toString();
                this.authCode = student.getAuthCode() == null ? "완료" : student.getAuthCode().toString();
                this.courseId = student.getCourse().getId();
            }
        }

        @Data
        class SubjectItem {
            private Long subjectId;
            private String code; // 능력단위 코드
            private String title;
            private String purpose;
            private String ncsType;
            private Integer grade;
            private Integer totalTime;
            private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 연번
            private String learningWay; // 학습 방법
            private String evaluationWay; // 평가 방법
            private String evaluationDate; // 평가일
            private String revaluationDate; // 재평가일
            private LocalDate startDate; // 교과목 시작 날짜
            private LocalDate endDate; // 교과목 종료 날짜
            private Long courseId; // 과정 PK


            public SubjectItem(Subject subject) {
                Paper paper = subject.getPapers().stream().filter(p -> !p.isReEvaluation()).findFirst().orElse(null);
                Paper rePaper = subject.getPapers().stream().filter(Paper::isReEvaluation).findFirst().orElse(null);

                this.subjectId = subject.getId();
                this.code = subject.getCode();
                this.title = subject.getTitle();
                this.purpose = subject.getPurpose();
                this.ncsType = subject.getNcsType().toKorean();
                this.grade = subject.getGrade();
                this.totalTime = subject.getTotalTime();
                this.no = subject.getNo();
                this.learningWay = subject.getLearningWay().toKorean();

                if (paper == null) {
                    this.evaluationWay = "시험지없음";
                    this.evaluationDate = "시험지없음";
                } else {
                    this.evaluationWay = paper.getEvaluationWay() != null
                            ? paper.getEvaluationWay().toKorean()
                            : "시험지없음";
                    this.evaluationDate = paper.getEvaluationDate().toString();
                }

                if (rePaper == null) {
                    this.revaluationDate = "시험지없음";
                } else {
                    this.revaluationDate = rePaper.getEvaluationDate().toString();
                }

                this.startDate = subject.getStartDate();
                this.endDate = subject.getEndDate();
                this.courseId = subject.getCourse().getId();
            }
        }
    }

}
