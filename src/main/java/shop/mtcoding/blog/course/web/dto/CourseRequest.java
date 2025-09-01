package shop.mtcoding.blog.course.web.dto;

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
            Long mainTeacherId, // 메인 강사 1명
            List<Long> subTeacherIds // 보조 강사 여러 명
    ) {
    }
}
