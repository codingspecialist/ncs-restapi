package shop.mtcoding.blog.course.web.dto;

import shop.mtcoding.blog.course.application.port.in.dto.SubjectCommand;
import shop.mtcoding.blog.course.domain.enums.LearningWay;
import shop.mtcoding.blog.course.domain.enums.NcsType;

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

        public SubjectCommand.Save toCommand() {
            return new SubjectCommand.Save(
                    this.courseTeacherId,
                    this.code,
                    this.title,
                    this.purpose,
                    this.ncsType,
                    this.gradeLevel,
                    this.totalTime,
                    this.no,
                    this.scorePolicy,
                    this.learningWay,
                    this.startDate,
                    this.endDate
            );
        }
    }
}
