package shop.mtcoding.blog.course.application.port.in;

import org.springframework.data.domain.Pageable;
import shop.mtcoding.blog.course.application.port.in.dto.CourseOutput;

public interface GetCoursesUseCase {
    CourseOutput.MaxPage 과정목록(Long teacherId, Pageable pageable);
}
