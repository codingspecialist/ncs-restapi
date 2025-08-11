package shop.mtcoding.blog.course.application.service.dto;

import shop.mtcoding.blog.course.application.domain.Subject;

import java.util.List;

public class SubjectOutput {
    public record MaxList(List<Subject> subjects) {
    }
}
