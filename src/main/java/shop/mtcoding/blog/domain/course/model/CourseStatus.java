package shop.mtcoding.blog.domain.course.model;

public enum CourseStatus {
    NOT_STARTED,  // 과정진행전
    RUNNING,      // 과정진행중
    FINISHED;     // 과정종료

    public String toKorean() {
        return switch (this) {
            case NOT_STARTED -> "과정진행전";
            case RUNNING -> "과정진행중";
            case FINISHED -> "과정종료";
        };
    }

    public static CourseStatus fromKorean(String value) {
        return switch (value) {
            case "과정진행전" -> NOT_STARTED;
            case "과정진행중" -> RUNNING;
            case "과정종료" -> FINISHED;
            default -> throw new IllegalArgumentException("지원하지 않는 과정 상태입니다: " + value);
        };
    }
}
