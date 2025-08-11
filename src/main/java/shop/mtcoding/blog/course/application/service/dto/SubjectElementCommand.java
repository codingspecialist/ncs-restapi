package shop.mtcoding.blog.course.application.service.dto;

import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;

public class SubjectElementCommand {
    public record Save(
            Integer no,
            String title,
            String criterion
    ) {
        public SubjectElement toEntity(Subject subject) {
            return SubjectElement.builder()
                    .no(no)
                    .title(title)
                    .criterion(criterion)
                    .subject(subject)
                    .build();
        }
    }

}
