package shop.mtcoding.blog.course.web.dto;

import shop.mtcoding.blog.course.application.domain.Subject;

import java.util.List;

public class SubjectResponse {
    public record MaxList(List<Subject> subjects) {
    }
}
