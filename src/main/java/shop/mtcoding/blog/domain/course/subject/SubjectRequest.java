package shop.mtcoding.blog.domain.course.subject;

import lombok.Data;
import shop.mtcoding.blog.domain.course.Course;

import java.time.LocalDate;

public class SubjectRequest {

    @Data
    public static class SaveDTO {
        private String teacherName;
        private String code; // 교과목ID
        private String title; // 교과목명
        private String purpose; // 교과목목표
        private String gubun; // NCS, 비NCS
        private Integer grade; // 수준
        private Integer totalTime; // 능력단위 시간
        private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 연번
        private String learningWay; // 학습 방법
        private LocalDate startDate; // 교과목 시작일
        private LocalDate endDate; // 교과목 종료일

        public Subject toEntity(Course course) {
            return Subject.builder()
                    .teacherName(teacherName)
                    .code(code)
                    .title(title)
                    .purpose(purpose)
                    .gubun(gubun)
                    .grade(grade)
                    .totalTime(totalTime)
                    .no(no)
                    .learningWay(learningWay)
                    .startDate(startDate)
                    .endDate(endDate)
                    .course(course)
                    .build();
        }
    }
}
