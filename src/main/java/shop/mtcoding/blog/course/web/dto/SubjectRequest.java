package shop.mtcoding.blog.course.web.dto;


import shop.mtcoding.blog.course.application.domain.enums.LearningWay;
import shop.mtcoding.blog.course.application.domain.enums.NcsType;


import java.time.LocalDate;

public class SubjectRequest {

    public record Save(
            Long courseTeacherId,
            String code,
            String title,
            String purpose,
            NcsType ncsType,
            Integer gradeLevel,
            Integer totalTime,
            Integer no,
            Double scorePolicy,
            LearningWay learningWay,
            LocalDate startDate,
            LocalDate endDate
    ) {
    }
}
