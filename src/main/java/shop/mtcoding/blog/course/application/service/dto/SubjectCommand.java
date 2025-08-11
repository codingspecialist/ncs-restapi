package shop.mtcoding.blog.course.application.service.dto;


import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.CourseTeacher;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.enums.LearningWay;
import shop.mtcoding.blog.course.application.domain.enums.NcsType;

import java.time.LocalDate;

public class SubjectCommand {

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
        public Subject toEntity(Course course, CourseTeacher courseTeacher) {
            return Subject.builder()
                    .code(code)
                    .title(title)
                    .purpose(purpose)
                    .ncsType(ncsType)
                    .gradeLevel(gradeLevel)
                    .totalTime(totalTime)
                    .scorePolicy(scorePolicy)
                    .learningWay(learningWay)
                    .startDate(startDate)
                    .endDate(endDate)
                    .courseTeacher(courseTeacher)
                    .course(course)
                    .build();
        }
    }
}
