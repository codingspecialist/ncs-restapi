package shop.mtcoding.blog.web.exam.teacher;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.Course;

import java.time.LocalDate;
import java.util.List;

public class TeacherExamResponse {

    @Data
    public static class CourseListDTO {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부
        private List<DTO> courses;

        public CourseListDTO(Page<Course> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.courses = paging.getContent().stream().map(DTO::new).toList();
        }

        @Data
        class DTO {
            private Long id;
            private String title;
            private String code;
            private Integer totalTime;
            private Integer totalDay;
            private Integer round;
            private Integer level;
            private String purpose;
            private LocalDate startDate;
            private LocalDate endDate;
            private String teacherName;
            private String courseStatus;

            public DTO(Course course) {
                this.id = course.getId();
                this.title = course.getTitle();
                this.code = course.getCode();
                this.totalTime = course.getTotalTime();
                this.totalDay = course.getTotalDay();
                this.round = course.getRound();
                this.level = course.getLevel();
                this.purpose = course.getPurpose();
                this.startDate = course.getStartDate();
                this.endDate = course.getEndDate();
                this.teacherName = course.getMainTeacherName();
                this.courseStatus = course.getCourseStatus().getValue();
            }
        }
    }

}
