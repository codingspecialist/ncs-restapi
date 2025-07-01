package shop.mtcoding.blog.webv2.course.subject;

import lombok.Data;
import shop.mtcoding.blog.domainv2222222.course.Course;
import shop.mtcoding.blog.domainv2222222.course.subject.LearningWay;
import shop.mtcoding.blog.domainv2222222.course.subject.NcsType;
import shop.mtcoding.blog.domainv2222222.course.subject.Subject;
import shop.mtcoding.blog.domainv2222222.user.teacher.Teacher;

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
        private Double scorePolicy;
        private LearningWay learningWay;  // "이론", "실습", "이론+실습"
        private LocalDate startDate;
        private LocalDate endDate;

        public Subject toEntity(Course course, Teacher teacher) {
            return Subject.builder()
                    .teacher(teacher)
                    .code(code)
                    .title(title)
                    .purpose(purpose)
                    .ncsType(ncsType)         // ✅ enum으로 변환
                    .gradeLevel(grade)
                    .totalTime(totalTime)
                    .no(no)
                    .learningWay(learningWay)   // ✅ enum으로 변환
                    .startDate(startDate)
                    .endDate(endDate)
                    .scorePolicy(scorePolicy)
                    .course(course)
                    .build();
        }
    }

}
