package shop.mtcoding.blog.web.course.student;

import lombok.Data;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.student.Student;
import shop.mtcoding.blog.domain.course.student.StudentStatus;

public class CourseStudentRequest {

    @Data
    public static class SaveDTO {
        private String name;
        private String birthday;

        public Student toEntity(Course course, String authCode) {
            return Student.builder()
                    .birthday(birthday)
                    .studentStatus(StudentStatus.ENROLL)
                    .name(name)
                    .course(course)
                    .authCode(authCode)
                    .build();
        }
    }
}
