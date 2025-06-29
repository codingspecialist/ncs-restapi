package shop.mtcoding.blog.web.user;

import lombok.Data;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserType;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentStatus;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

public class UserRequest {

    @Data
    public static class TeacherJoin {
        private String username; // 3~20 자 사이로 받아야 함
        private String password;
        private String email;
        private String name;
        private UserType role;
        private String sign;

        public User toEntity() {
            Teacher teacher = Teacher.builder()
                    .name(name)
                    .sign(sign)
                    .build();
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .teacher(teacher)
                    .build();
        }
    }

    @Data
    public static class StudentJoin {
        private String username; // 3~20 자 사이로 받아야 함
        private String password;
        private String email;
        private String name;
        private UserType role;
        private Long courseId; // 학생일때만 받음
        private String birthday; // 학생일때만 받음

        public User toEntity(Course course, String authCode) {
            Student student = Student.builder()
                    .studentStatus(StudentStatus.ENROLL)
                    .course(course)
                    .birthday(birthday)
                    .authCode(authCode)
                    .isVerified(false)
                    .name(name)
                    .build();
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .student(student)
                    .build();
        }
    }

    @Data
    public static class Login {
        private String username;
        private String password;
    }
}
