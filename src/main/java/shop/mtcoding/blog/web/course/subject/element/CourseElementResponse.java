package shop.mtcoding.blog.web.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;

import java.util.List;

public class CourseElementResponse {

    @Data
    public static class ListDTO {
        private Long subjectId;
        private String title;
        private String purpose;

        List<ElementDTO> subjectElements;

        public ListDTO(Subject subject, List<SubjectElement> subjectElements) {
            this.subjectId = subject.getId();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.subjectElements = subjectElements.stream().map(ElementDTO::new).toList();
        }

        @Data
        class ElementDTO {
            private Long subjectElementId;
            private Integer no;
            private String subtitle;
            private String subjectElementPurpose;

            public ElementDTO(SubjectElement subjectElement) {
                this.subjectElementId = subjectElement.getId();
                this.no = subjectElement.getNo();
                this.subtitle = subjectElement.getSubtitle();
                this.subjectElementPurpose = subjectElement.getSubjectElementPurpose();
            }
        }
    }
}
