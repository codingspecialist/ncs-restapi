package shop.mtcoding.blog.domain.user.application.dto;

import shop.mtcoding.blog.domain.user.model.UserType;

public class UserCommand {

    public record TeacherJoin(String username, String password, String email, String name, UserType role, String sign) {
    }

    public record StudentJoin(String username, String password, String email, String name, UserType role, Long courseId,
                              String birthday) {
    }

    public record EmpJoin(String username, String password, String email, String name, UserType role, String sign) {
    }

    public record Login(String username, String password) {
    }
}