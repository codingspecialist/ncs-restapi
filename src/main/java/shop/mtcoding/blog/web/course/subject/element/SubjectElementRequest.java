package shop.mtcoding.blog.web.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;

public class SubjectElementRequest {

    @Data
    public static class Save {
        private Integer no;
        private String subtitle;
        private String purpose;

        public SubjectElement toEntity(Subject subject) {
            return SubjectElement.builder()
                    .no(no)
                    .subtitle(subtitle)
                    .purpose(purpose)
                    .subject(subject)
                    .build();
        }
    }
}
