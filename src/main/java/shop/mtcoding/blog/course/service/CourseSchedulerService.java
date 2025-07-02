package shop.mtcoding.blog.course.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.model.Course;
import shop.mtcoding.blog.course.port.in.CourseStatusUpdateUseCase;
import shop.mtcoding.blog.course.port.out.CourseRepositoryPort;
import shop.mtcoding.blog.course.type.CourseStatus;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseSchedulerService implements CourseStatusUpdateUseCase {

    private final CourseRepositoryPort courseRepository;

    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    @Transactional
    @Override
    public void updateCourseStatus() {
        List<Course> courses = courseRepository.findAllNotFinished();

        for (Course course : courses) {
            CourseStatus courseStatus = MyUtil.courseStatusUpdate(course.getStartDate(), course.getEndDate());
            course.setCourseStatus(courseStatus);
        }
    }
}

