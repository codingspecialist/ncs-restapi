package shop.mtcoding.blog.course.application.port.in.dto;

import java.time.LocalDate;
import java.util.List;

public class CourseCommand {
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
            Long mainTeacherId,
            List<Long> subTeacherIds
    ) {

    }
}
