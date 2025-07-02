package shop.mtcoding.blog.user.application.port.out;

import shop.mtcoding.blog.course.domain.Course;

public interface CourseLoadPort {
    Course loadCourse(Long courseId);
}
