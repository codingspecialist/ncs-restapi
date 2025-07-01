package shop.mtcoding.blog.domain.course.model;

import lombok.Getter;

@Getter
public enum CourseTeacherEnum {
    MAIN("메인강사"), SUB("보조강사");

    private String value;

    CourseTeacherEnum(String value) {
        this.value = value;
    }
}
