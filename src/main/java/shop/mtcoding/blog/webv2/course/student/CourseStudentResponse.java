package shop.mtcoding.blog.webv2.course.student;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domainv2222222.user.student.Student;

import java.util.List;

public class CourseStudentResponse {

    @Data
    public static class PagingDTO {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부

        private List<StudentDTO> students;

        public PagingDTO(Page<Student> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.students = paging.getContent().stream().map(StudentDTO::new).toList();
        }

        @Data
        class StudentDTO {
            private Long studentId;
            private String name; // 학생 번호는 이름순으로 해서 rownum 뽑자
            private String birthday;
            private String state; // 취업, 중도탈락, 미이수, 이수, 재학중
            private String dropOutDate; // 중탈 날짜
            private String dropOutReason; // 중탈 이유
            private String comment; // 학생 모든 교과목에 대한 총평
            private String grade; // 학생 모든 교과목에 대한 수준 1,2,3,4,5
            private Long courseId;
            private String courseTitle;
            private Integer courseRound;

            public StudentDTO(Student student) {
                this.studentId = student.getId();
                this.name = student.getName();
                this.birthday = student.getBirthday();
                this.state = student.getStudentStatus().toKorean();
                this.dropOutDate = student.getDropOutDate() == null ? "" : student.getDropOutDate().toString();
                this.dropOutReason = student.getDropOutReason() == null ? "" : student.getDropOutReason();
                this.comment = student.getComment() == null ? "" : student.getComment();
                this.grade = student.getGradeLevel() == null ? "" : student.getGradeLevel().toString();
                this.courseId = student.getCourse().getId();
                this.courseTitle = student.getCourse().getTitle();
                this.courseRound = student.getCourse().getRound();
            }
        }
    }
}
