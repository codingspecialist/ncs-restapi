package shop.mtcoding.blog.domain.course.model;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.application.port.out.FindCoursePort;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class CourseStatusScheduler {

    private final FindCoursePort findCoursePort;

    //@Scheduled(cron = "*/20 * * * * *") // 매 20초마다 실행
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시
    @Transactional
    public void updateCourseStatus() {
        List<Course> courses = findCoursePort.findAllNotFinished();

        for (Course course : courses) {
            CourseStatus courseStatus = MyUtil.courseStatusUpdate(course.getStartDate(), course.getEndDate());
            course.setCourseStatus(courseStatus);
        }
    }
}

