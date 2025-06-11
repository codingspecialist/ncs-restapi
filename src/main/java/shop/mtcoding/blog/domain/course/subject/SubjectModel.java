package shop.mtcoding.blog.domain.course.subject;

import java.util.List;

public class SubjectModel {
    public record Items(List<Subject> subjects) {
    }
}
