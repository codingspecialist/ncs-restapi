package shop.mtcoding.blog.user.adapter.out.external;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.course.adapter.out.persistence.CourseRepository;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.user.application.port.out.CourseLoadPort;

@RequiredArgsConstructor
@Component
public class CourseRepositoryAdapter implements CourseLoadPort {

    private final CourseRepository courseRepository;

    @Override
    public Course loadCourse(Long courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }
}
