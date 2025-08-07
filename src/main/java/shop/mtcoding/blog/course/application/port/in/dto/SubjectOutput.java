package shop.mtcoding.blog.course.application.port.in.dto;


import shop.mtcoding.blog.course.domain.Subject;

import java.util.List;

public class SubjectOutput {
    public record MaxList(List<Subject> subjects) {
    }
}
