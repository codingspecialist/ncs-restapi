package shop.mtcoding.blog.web.course;

import lombok.Data;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.CourseStatus;

import java.time.LocalDate;
import java.util.List;

public class CourseRequest {

    @Data
    public static class SaveDTO {
        private String code;
        private String title;
        private Integer level;
        private String purpose;
        private Integer totalTime;
        private Integer totalDay;
        private Integer round;
        private LocalDate startDate;
        private LocalDate endDate;
        private Long mainTeacherId;         // 메인 강사 1명
        private List<Long> subTeacherIds;   // 보조 강사 여러 명

        public Course toEntity(String mainTeacherName) {

            CourseStatus courseStatus = MyUtil.courseStatusUpdate(startDate, endDate);

            return Course.builder()
                    .code(code)
                    .title(title)
                    .level(level)
                    .purpose(purpose)
                    .totalTime(totalTime)
                    .totalDay(totalDay)
                    .round(round)
                    .startDate(startDate)
                    .endDate(endDate)
                    .courseStatus(courseStatus)
                    .mainTeacherName(mainTeacherName)
                    .build();
        }
    }
}
