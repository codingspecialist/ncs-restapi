package shop.mtcoding.blog.domain.course.student;

public enum StudentStatus {
    EMPLOYED,     // 취업
    DROPOUT,      // 중도탈락
    INCOMPLETE,   // 미이수
    COMPLETED,    // 이수
    ENROLLED;     // 재학중

    public String toKorean() {
        return switch (this) {
            case EMPLOYED -> "취업";
            case DROPOUT -> "중도탈락";
            case INCOMPLETE -> "미이수";
            case COMPLETED -> "이수";
            case ENROLLED -> "재학중";
        };
    }

    public static StudentStatus fromKorean(String value) {
        return switch (value) {
            case "취업" -> EMPLOYED;
            case "중도탈락" -> DROPOUT;
            case "미이수" -> INCOMPLETE;
            case "이수" -> COMPLETED;
            case "재학중" -> ENROLLED;
            default -> throw new IllegalArgumentException("지원하지 않는 학생 상태입니다: " + value);
        };
    }
}
