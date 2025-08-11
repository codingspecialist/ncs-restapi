package shop.mtcoding.blog.course.application.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.enums.CourseStatus;
import shop.mtcoding.blog.course.application.repository.CourseRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseSchedulerService {

    private final CourseRepository courseRepository;

    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    @Transactional
    public void updateCourseStatus() {
        List<Course> courses = courseRepository.findAllNotFinished();

        for (Course course : courses) {
            CourseStatus courseStatus = MyUtil.courseStatusUpdate(course.getStartDate(), course.getEndDate());
            course.setCourseStatus(courseStatus);
        }
    }
}

