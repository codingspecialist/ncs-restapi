package shop.mtcoding.blog.user.adapter.in.web.dto;

// Lombok 어노테이션은 레코드가 자동 생성하는 기능과 겹치므로 제거하거나 최소화
// import lombok.AllArgsConstructor;
// import lombok.Data;

import shop.mtcoding.blog.user.domain.Emp;
import shop.mtcoding.blog.user.domain.Student;
import shop.mtcoding.blog.user.domain.Teacher;
import shop.mtcoding.blog.user.domain.User;
import shop.mtcoding.blog.user.domain.enums.StudentStatus;
import shop.mtcoding.blog.user.domain.enums.UserRole;

public class UserResponse {

    public record Item(Long id, String username, String email, String role) {
        public static Item from(User user) {
            return new Item(user.getId(), user.getUsername(), user.getEmail(), user.getRole().toKorean());
        }
    }

    public record StudentItem(
            Long studentId,
            String name,
            String birthday,
            StudentStatus studentStatus,
            Boolean isVerified
    ) {
        public static StudentItem from(Student student) {
            return new StudentItem(
                    student.getId(),
                    student.getName(),
                    student.getBirthday(),
                    student.getStudentStatus(),
                    student.getIsVerified()
            );
        }
    }

    public record TeacherItem(
            Long teacherId,
            String sign,
            String name
    ) {
        public static TeacherItem from(Teacher teacher) {
            return new TeacherItem(
                    teacher.getId(),
                    teacher.getSign(),
                    teacher.getName()
            );
        }
    }

    public record EmpItem(
            Long empId,
            String sign,
            String name
    ) {
        public static EmpItem from(Emp emp) {
            return new EmpItem(
                    emp.getId(),
                    emp.getSign(),
                    emp.getName()
            );
        }
    }

    public record SessionItem(
            Long id,
            String username,
            String email,
            UserRole role,
            String accessToken,
            String refreshToken,
            StudentItem student,
            TeacherItem teacher,
            EmpItem emp
    ) {
        public static SessionItem from(User user, String accessToken, String refreshToken) {
            StudentItem studentItem = null;
            TeacherItem teacherItem = null;
            EmpItem empItem = null;

            if (user.getRole() == UserRole.STUDENT) {
                studentItem = StudentItem.from(user.getStudent());
            } else if (user.getRole() == UserRole.TEACHER) {
                teacherItem = TeacherItem.from(user.getTeacher());
            } else if (user.getRole() == UserRole.EMP) {
                empItem = EmpItem.from(user.getEmp());
            }

            return new SessionItem(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    accessToken,
                    refreshToken,
                    studentItem,
                    teacherItem,
                    empItem
            );
        }
    }


}