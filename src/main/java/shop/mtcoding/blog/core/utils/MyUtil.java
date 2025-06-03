package shop.mtcoding.blog.core.utils;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.domain.course.CourseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
public class MyUtil {

    public static String localDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String localDateTimeToString(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String generateAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    public static CourseStatus courseStatusUpdate(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (endDate.isBefore(today)) {
            log.info("Updating course status - FINISHED");
            return CourseStatus.FINISHED;
        } else if (!startDate.isAfter(today) && !endDate.isBefore(today)) {
            log.info("Updating course status - RUNNING");
            return CourseStatus.RUNNING;
        } else {
            log.info("Updating course status - NOT_STARTED");
            return CourseStatus.NOT_STARTED;
        }
    }
}
