package shop.mtcoding.blog.core.utils;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog.domain.course.CourseEnum;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
public class MyUtil {

    public static String localDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String generateAuthCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        return String.valueOf(code);
    }

    public static CourseEnum courseStatusUpdate(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (endDate.isBefore(today)) {
            log.info("Updating course status - FINISHED");
            return CourseEnum.FINISHED;
        } else if (!startDate.isAfter(today) && !endDate.isBefore(today)) {
            log.info("Updating course status - RUNNING");
            return CourseEnum.RUNNING;
        } else {
            log.info("Updating course status - NOT_STARTED");
            return CourseEnum.NOT_STARTED;
        }
    }
}
