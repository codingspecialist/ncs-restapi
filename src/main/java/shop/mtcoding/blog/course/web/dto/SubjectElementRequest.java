package shop.mtcoding.blog.course.web.dto;

public class SubjectElementRequest {

    public record Save(
            Integer no,
            String title,
            String criterion) {

    }
}