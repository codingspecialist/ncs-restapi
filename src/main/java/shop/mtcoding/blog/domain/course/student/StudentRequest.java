package shop.mtcoding.blog.domain.course.student;

import lombok.Data;
import shop.mtcoding.blog.domain.course.Course;

public class StudentRequest {

    @Data
    public static class SaveDTO {
        private String name;
        private String birthday;

        public Student toEntity(Course course, String authCode) {
            return Student.builder()
                    .birthday(birthday)
                    .state(StudentEnum.ENROLLED)
                    .name(name)
                    .course(course)
                    .authCode(authCode)
                    .build();
        }
    }
}
