package shop.mtcoding.blog.course.application.port.in.dto;


import shop.mtcoding.blog.course.domain.Subject;

public class SubjectElementOutput {
    public record MaxList(Subject subject) {
    }
}
