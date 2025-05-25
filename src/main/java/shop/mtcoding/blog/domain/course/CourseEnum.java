package shop.mtcoding.blog.domain.course;

import lombok.Getter;

@Getter
public enum CourseEnum {
    NOT_STARTED("과정진행전"), RUNNING("과정진행중"), FINISHED("과정종료");

    private String value;

    CourseEnum(String value) {
        this.value = value;
    }
}
