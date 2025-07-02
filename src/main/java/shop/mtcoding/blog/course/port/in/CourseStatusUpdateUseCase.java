package shop.mtcoding.blog.course.port.in;

public interface CourseStatusUpdateUseCase {
    //@Scheduled(cron = "*/20 * * * * *") // 매 20초마다 실행
    void updateCourseStatus();
}
