package shop.mtcoding.blog.web.course.subject.element;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;

import java.util.List;

public class SubjectElementResponse {

    @Data
    public static class Item {
        private Long subjectElementId;
        private Integer no;
        private String title;
        private String criterion;

        public Item(SubjectElement subjectElement) {
            this.subjectElementId = subjectElement.getId();
            this.no = subjectElement.getNo();
            this.title = subjectElement.getTitle();
            this.criterion = subjectElement.getCriterion();
        }
    }

    @Data
    public static class Items {
        private Long subjectId;
        private String title;
        private String purpose;

        List<Item> elements;

        public Items(Subject subject) {
            this.subjectId = subject.getId();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.elements = subject.getElements().stream().map(Item::new).toList();
        }
    }
}
