package shop.mtcoding.blog.web.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;

public class CourseElementRequest {

    @Data
    public static class SaveDTO {
        private Integer no;
        private String subtitle;

        public SubjectElement toEntity(Subject subject) {
            return SubjectElement.builder()
                    .no(no)
                    .subtitle(subtitle)
                    .subject(subject)
                    .build();
        }
    }
}
