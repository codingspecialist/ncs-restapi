package shop.mtcoding.blog.user;

import lombok.Getter;

@Getter
public enum UserEnum {
    STUDENT("학생"),
    TEACHER("강사"),
    BASIC_EMP("직원"),
    MANAGER_EMP("팀장"),
    SUPER_EMP("원장"),
    ADMIN("관리자");

    private String value;

    UserEnum(String value) {
        this.value = value;
    }
}
