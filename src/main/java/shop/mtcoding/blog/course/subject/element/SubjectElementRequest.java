package shop.mtcoding.blog.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.course.subject.Subject;

public class SubjectElementRequest {

    @Data
    public static class SaveDTO {
        private Integer no;
        private String subtitle;

        public SubjectElement toEntity(Subject subject){
            return SubjectElement.builder()
                    .no(no)
                    .subtitle(subtitle)
                    .subject(subject)
                    .build();
        }
    }
}
