package shop.mtcoding.blog.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.course.application.port.in.GetCoursesUseCase;
import shop.mtcoding.blog.course.application.port.in.dto.CourseOutput;
import shop.mtcoding.blog.course.application.port.out.CourseRepositoryPort;
import shop.mtcoding.blog.course.domain.Course;

@RequiredArgsConstructor
@Service
public class CourseService implements GetCoursesUseCase {

    private final CourseRepositoryPort courseRepositoryPort;

    @Override
    public CourseOutput.MaxPage 과정목록(Long teacherId, Pageable pageable) {
        Page<Course> coursePG = courseRepositoryPort.findAllByTeacherId(teacherId, pageable);
        return new CourseOutput.MaxPage(coursePG);
    }


}
