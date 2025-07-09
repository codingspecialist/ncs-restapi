package shop.mtcoding.blog.course.adapter.in.web.dto;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.course.domain.Course;

import java.time.LocalDate;
import java.util.List;

public class CourseResponse {
    public record Max(
            Long courseId,
            String code,
            String title,
            Integer level,
            Integer round,
            String purpose,
            Integer totalTime,
            Integer totalDay,
            LocalDate startDate,
            LocalDate endDate,
            String courseStatus,
            String mainTeacherName
    ) {
        // 내부 사용을 위한 private 생성자
        private Max(Course course) {
            this(course.getId(),
                    course.getCode(),
                    course.getTitle(),
                    course.getLevel(),
                    course.getRound(),
                    course.getPurpose(),
                    course.getTotalTime(),
                    course.getTotalDay(),
                    course.getStartDate(),
                    course.getEndDate(),
                    course.getCourseStatus().toKorean(),
                    course.getMainTeacherName()
            );
        }

        public static Max from(Course course) {
            return new Max(course);
        }
    }

    public record MaxPage(
            Integer totalPage,
            Integer pageSize,
            Integer pageNumber,
            Boolean isFirst,
            Boolean isLast,
            List<Max> courses
    ) {
        // 내부 사용을 위한 private 생성자
        private MaxPage(Page<Course> paging) {
            this(
                    paging.getTotalPages(),
                    paging.getSize(),
                    paging.getNumber(),
                    paging.isFirst(),
                    paging.isLast(),
                    paging.getContent().stream()
                            .map(Max::from) // 여기에서 from 메서드를 사용합니다!
                            .toList()
            );
        }

        public static MaxPage from(Page<Course> paging) {
            return new MaxPage(paging);
        }
    }
}