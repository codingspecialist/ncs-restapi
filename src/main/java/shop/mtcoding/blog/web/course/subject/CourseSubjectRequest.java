package shop.mtcoding.blog.web.course.subject;

import lombok.Data;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.subject.LearningWay;
import shop.mtcoding.blog.domain.course.subject.NcsType;
import shop.mtcoding.blog.domain.course.subject.Subject;

import java.time.LocalDate;

public class CourseSubjectRequest {

    @Data
    public static class SaveDTO {
        private String teacherName;
        private String code;         // 교과목 ID
        private String title;        // 교과목명
        private String purpose;      // 교과목 목표
        private NcsType ncsType;      // "NCS", "비NCS"
        private Integer grade;       // 수준
        private Integer totalTime;   // 능력단위 시간
        private Integer no;          // 순번
        private LearningWay learningWay;  // "이론", "실습", "이론+실습"
        private LocalDate startDate;
        private LocalDate endDate;

        public Subject toEntity(Course course) {
            return Subject.builder()
                    .teacherName(teacherName)
                    .code(code)
                    .title(title)
                    .purpose(purpose)
                    .ncsType(ncsType)         // ✅ enum으로 변환
                    .grade(grade)
                    .totalTime(totalTime)
                    .no(no)
                    .learningWay(learningWay)   // ✅ enum으로 변환
                    .startDate(startDate)
                    .endDate(endDate)
                    .course(course)
                    .build();
        }
    }

}
