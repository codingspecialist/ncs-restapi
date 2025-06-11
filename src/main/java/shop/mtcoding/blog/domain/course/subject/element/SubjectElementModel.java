package shop.mtcoding.blog.domain.course.subject.element;

import shop.mtcoding.blog.domain.course.subject.Subject;

import java.util.List;

public class SubjectElementModel {
    public record Items(
            Subject subject,
            List<SubjectElement> subjectElements
    ) {
    }
}
