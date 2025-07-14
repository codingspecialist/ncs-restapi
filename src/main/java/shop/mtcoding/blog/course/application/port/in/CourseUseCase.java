package shop.mtcoding.blog.course.application.port.in;

import org.springframework.data.domain.Pageable;
import shop.mtcoding.blog.course.application.port.in.dto.CourseCommand;
import shop.mtcoding.blog.course.application.port.in.dto.CourseOutput;

public interface CourseUseCase {
    CourseOutput.MaxPage 과정목록(Long teacherId, Pageable pageable);

    CourseOutput.Max 과정등록(CourseCommand.Save command);

    CourseOutput.Max 과정정보(Long courseId);

    CourseOutput.Detail 과정상세(Long courseId);
}
