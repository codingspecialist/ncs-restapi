package shop.mtcoding.blog.web.user;

import lombok.Data;
import shop.mtcoding.blog.domain.user.User;
import shop.mtcoding.blog.domain.user.UserType;
import shop.mtcoding.blog.domain.user.student.Student;
import shop.mtcoding.blog.domain.user.student.StudentStatus;
import shop.mtcoding.blog.domain.user.teacher.Teacher;

public class UserResponse {

    @Data
    public static class Item {
        private Long id;
        private String username;
        private String email;
        private UserType role;

        public Item(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
        }
    }

    @Data
    public static class Session {
        private Long id;
        private String username;
        private String email;
        private UserType role;
        private String accessToken;
        private String refreshToken;

        private StudentItem student;
        private TeacherItem teacher;

        public Session(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            if (user.getRole() == UserType.STUDENT) {
                this.student = new StudentItem(user.getStudent()); // lazy loading
            } else if (user.getRole() == UserType.TEACHER) {
                this.teacher = new TeacherItem(user.getTeacher()); // lazy loading
            }
        }

        public Session(User user, String accessToken, String refreshToken) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            if (user.getRole() == UserType.STUDENT) {
                this.student = new StudentItem(user.getStudent()); // lazy loading
            } else if (user.getRole() == UserType.TEACHER) {
                this.teacher = new TeacherItem(user.getTeacher()); // lazy loading
            }
        }

        @Data
        class StudentItem {
            private Long studentId;
            private Long courseId;
            private String name;
            private String birthday;
            private StudentStatus studentStatus; // EMPLOY, ENROLL, DROPOUT
            private Boolean isVerified;

            public StudentItem(Student student) {
                this.studentId = student.getId();
                this.courseId = student.getCourse().getId();
                this.name = student.getName();
                this.birthday = student.getBirthday();
                this.studentStatus = student.getStudentStatus();
                this.isVerified = student.getIsVerified();
            }
        }

        @Data
        class TeacherItem {
            private Long teacherId;
            private String sign;
            private String name;

            public TeacherItem(Teacher teacher) {
                this.teacherId = teacher.getId();
                this.sign = teacher.getSign();
                this.name = teacher.getName();
            }
        }
    }
}
