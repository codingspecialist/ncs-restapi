package shop.mtcoding.blog.course.domain.enums;

import lombok.Getter;

@Getter
public enum TeacherType {
    MAIN("메인강사"), SUB("보조강사");

    private String value;

    TeacherType(String value) {
        this.value = value;
    }
}
