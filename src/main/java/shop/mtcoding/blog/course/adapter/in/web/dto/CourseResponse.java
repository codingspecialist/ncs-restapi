package shop.mtcoding.blog.course.adapter.in.web.dto;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.course.domain.CourseStudent;
import shop.mtcoding.blog.course.domain.CourseSubject;

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


    public record Detail(
            Max course,
            List<SubjectMax> subjects,
            List<StudentMax> students
    ) {
        private Detail(Course course, List<CourseSubject> courseSubjects, List<CourseStudent> students) {
            this(
                    new Max(course),
                    courseSubjects.stream().map(SubjectMax::new).toList(),
                    students.stream().map(StudentMax::new).toList()
            );
        }

        public static Detail from(Course course, List<CourseSubject> courseSubjects, List<CourseStudent> students) {
            return new Detail(course, courseSubjects, students);
        }


        public record StudentMax(
                Long studentId,
                String name,
                String birthday,
                String studentStatus,
                String dropOutDate,
                String dropOutReason,
                String comment,
                String grade,
                String authCode,
                Long courseId
        ) {
            private StudentMax(CourseStudent courseStudent) { // Student 엔티티 대신 CourseStudent 엔티티를 받도록 변경
                this(
                        courseStudent.getStudent().getId(), // CourseStudent에서 실제 Student 엔티티 접근
                        courseStudent.getStudent().getName(),
                        courseStudent.getStudent().getBirthday(),
                        courseStudent.getStudent().getStudentStatus().toKorean(),
                        courseStudent.getStudent().getDropOutDate() == null ? "" : courseStudent.getStudent().getDropOutDate().toString(),
                        courseStudent.getStudent().getDropOutReason() == null ? "" : courseStudent.getStudent().getDropOutReason(),
                        courseStudent.getStudent().getComment() == null ? "" : courseStudent.getStudent().getComment(),
                        courseStudent.getStudent().getGradeLevel() == null ? "" : courseStudent.getStudent().getGradeLevel().toString(),
                        courseStudent.getStudent().getAuthCode() == null ? "완료" : courseStudent.getStudent().getAuthCode().toString(),
                        courseStudent.getCourse().getId() // CourseStudent에서 실제 Course 엔티티 접근
                );
            }
        }

        // 평가일, 재평가일 이 부분은 추후 리팩토링 하자.
        public record SubjectMax(
                Long subjectId,
                String code,
                String title,
                String purpose,
                String ncsType,
                Integer gradeLevel,
                Integer totalTime,
                Integer no,
                String learningWay,
                LocalDate startDate,
                LocalDate endDate,
                Long courseId
        ) {
            private SubjectMax(CourseSubject courseSubjects) {
                this(
                        courseSubjects.getId(),
                        courseSubjects.getSubject().getCode(),
                        courseSubjects.getSubject().getTitle(),
                        courseSubjects.getSubject().getPurpose(),
                        courseSubjects.getSubject().getNcsType().toKorean(),
                        courseSubjects.getSubject().getGradeLevel(),
                        courseSubjects.getTotalTime(),
                        courseSubjects.getNo(),
                        courseSubjects.getSubject().getLearningWay().toKorean(),
                        courseSubjects.getStartDate(),
                        courseSubjects.getEndDate(),
                        courseSubjects.getCourse().getId()
                );
            }
        }
    }
}