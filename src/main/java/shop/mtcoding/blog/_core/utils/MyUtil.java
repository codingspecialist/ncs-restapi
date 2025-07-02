package shop.mtcoding.blog._core.utils;

import lombok.extern.slf4j.Slf4j;
import shop.mtcoding.blog._core.errors.exception.api.Exception500;
import shop.mtcoding.blog.course.domain.enums.CourseStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    public static String fileWrite(String imgBase64) {
        try {
            // 1. file folder path
            String folder = "/images/";

            // 1. 파일명 생성
            UUID uuid = UUID.randomUUID();
            String mimeType = Base64Util.getMimeType(imgBase64);
            String imgFilename = folder + uuid + "." + mimeType;

            // 2. base64 -> byte[]
            byte[] imgBytes = Base64Util.decodeAsBytes(imgBase64);

            // 3. 파일 쓰기
            Path imgFilePath = Paths.get("." + imgFilename);
            Files.write(imgFilePath, imgBytes);
            return imgFilename;
        } catch (Exception e) {
            throw new Exception500("이미지 저장 오류 : " + e.getMessage());
        }
    }

    public static List<String> parseMultiline(String raw) {
        if (raw == null || raw.trim().isEmpty()) return Collections.emptyList();

        return Arrays.stream(raw.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> !s.isBlank())
                .toList();
    }

    public static List<String> parseMultilineWithoutHyphen(String raw) {
        if (raw == null || raw.trim().isEmpty()) return Collections.emptyList();

        return Arrays.stream(raw.split("\\r?\\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> !s.isBlank())
                .map(s -> s.replaceFirst("^-\\s*", ""))  // ← 하이픈+공백 제거
                .toList();
    }

    public static double scaleTo100(double score, double currentMaxScore) {
        if (currentMaxScore <= 0) {
            throw new IllegalArgumentException("현재 만점은 0보다 커야 합니다.");
        }
        double result = (score / currentMaxScore) * 100;
        return Math.round(result * 10) / 10.0;
    }

    public static String extractFirstLine(String text) {
        if (text == null || text.isBlank()) return "";

        int newlineIndex = text.indexOf('\n');
        if (newlineIndex == -1) {
            return text.trim(); // 줄바꿈이 없는 경우 전체 리턴
        }
        return text.substring(0, newlineIndex).trim(); // 첫 줄만 추출
    }

}
