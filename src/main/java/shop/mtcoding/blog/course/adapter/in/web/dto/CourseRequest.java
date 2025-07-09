package shop.mtcoding.blog.course.adapter.in.web.dto;

import shop.mtcoding.blog.course.application.port.in.dto.CourseCommand;

import java.time.LocalDate;
import java.util.List;

public class CourseRequest {

    public record Save(
            String code,
            String title,
            Integer level,
            Integer round,
            String purpose,
            Integer totalTime,
            Integer totalDay,
            LocalDate startDate,
            LocalDate endDate,
            Long mainTeacherId,         // 메인 강사 1명
            List<Long> subTeacherIds   // 보조 강사 여러 명
    ) {
        public CourseCommand.Save toCommand() {
            return new CourseCommand.Save(
                    this.code,
                    this.title,
                    this.level,
                    this.round,
                    this.purpose,
                    this.totalTime,
                    this.totalDay,
                    this.startDate,
                    this.endDate,
                    this.mainTeacherId,
                    this.subTeacherIds
            );
        }
    }
}
