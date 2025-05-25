package shop.mtcoding.blog.user;

import lombok.Getter;

// 개발회사에서 모든 학원에 대한 관리는 서버를 따로 만드는 것이 맞다!!
// ADMIN은 모든 주소에 접근할 수 있다.
@Getter
public enum UserEnum {
    STUDENT("학생"),
    TEACHER("강사"),
    EMP("직원"); // 사원, 팀장, 원장

    private String value;

    UserEnum(String value) {
        this.value = value;
    }
}
