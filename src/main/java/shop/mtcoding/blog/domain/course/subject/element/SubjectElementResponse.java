package shop.mtcoding.blog.domain.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;

import java.util.List;

public class SubjectElementResponse {

    @Data
    public static class ListDTO {
        private Long subjectId;
        private String title;
        private String purpose;

        List<SubjectElementDTO> subjectElements;

        public ListDTO(Subject subject, List<SubjectElement> subjectElements) {
            this.subjectId = subject.getId();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.subjectElements = subjectElements.stream().map(SubjectElementDTO::new).toList();
        }

        @Data
        class SubjectElementDTO {
            private Long subjectElementId;
            private Integer no;
            private String subtitle;

            public SubjectElementDTO(SubjectElement subjectElement) {
                this.subjectElementId = subjectElement.getId();
                this.no = subjectElement.getNo();
                this.subtitle = subjectElement.getSubtitle();
            }
        }
    }
}
