package shop.mtcoding.blog.course.web.dto;


import shop.mtcoding.blog.course.domain.Subject;
import shop.mtcoding.blog.course.domain.SubjectElement;

import java.util.List;

public class SubjectElementResponse {

    public record Max(
            Long subjectElementId,
            Integer no,
            String title,
            String criterion
    ) {
        public Max(SubjectElement subjectElement) {
            this(subjectElement.getId(),
                    subjectElement.getNo(),
                    subjectElement.getTitle(),
                    subjectElement.getCriterion());
        }
    }

    public record MaxList(
            Long subjectId,
            String title,
            String purpose,
            List<Max> elements
    ) {
        public MaxList(Subject subject) {
            this(subject.getId(),
                    subject.getTitle(),
                    subject.getPurpose(),
                    subject.getElements().stream().map(e -> new Max(e)).toList());
        }
    }
}
