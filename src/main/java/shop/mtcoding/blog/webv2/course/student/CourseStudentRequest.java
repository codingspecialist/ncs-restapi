package shop.mtcoding.blog.webv2.course.student;

import lombok.Data;
import shop.mtcoding.blog.domainv2222222.course.Course;
import shop.mtcoding.blog.domainv2222222.user.student.Student;
import shop.mtcoding.blog.domainv2222222.user.student.StudentStatus;

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
