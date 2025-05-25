package shop.mtcoding.blog.domain.course.subject.paper.question;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;

import java.util.List;

public class QuestionDBResponse {

    @Data
    public static class ExpectedNextDTO {
        private Integer expectNo;
        private Integer expectPoint;
        private List<ElementDTO> elements;

        public ExpectedNextDTO(Integer expectNo, Integer expectPoint) {
            this.expectNo = expectNo;
            this.expectPoint = expectPoint;
        }

        public void setElements(List<SubjectElement> elements) {
            this.elements = elements.stream().map(ElementDTO::new).toList();
        }

        @Data
        class ElementDTO {
            private Long elementId;
            private String subtitle;

            public ElementDTO(SubjectElement element) {
                this.elementId = element.getId();
                this.subtitle = element.getSubtitle();
            }
        }
    }

}
