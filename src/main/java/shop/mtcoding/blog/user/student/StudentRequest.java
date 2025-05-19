package shop.mtcoding.blog.user.student;

import lombok.Data;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.user.User;

public class StudentRequest {

    @Data
    public static class SaveDTO {
        private String name;
        private String birthday;
        private String authCode;

        public Student toEntity(Course course, User user) {
            return Student.builder()
                    .birthday(birthday)
                    .state(StudentEnum.ENROLLED)
                    .course(course)
                    .user(user)
                    .authCode(authCode)
                    .build();
        }
    }
}
