package shop.mtcoding.blog.webv2.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.domainv2222222.course.subject.Subject;
import shop.mtcoding.blog.domainv2222222.course.subject.element.SubjectElement;

public class SubjectElementRequest {

    @Data
    public static class Save {
        private Integer no;
        private String title;
        private String criterion;

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
