package shop.mtcoding.blog.domainv2222222.user.student;

public enum StudentStatus {
    EMPLOY,     // 취업
    DROPOUT,    // 중도탈락
    ENROLL;     // 재학중

    public String toKorean() {
        return switch (this) {
            case EMPLOY -> "취업";
            case DROPOUT -> "중도탈락";
            case ENROLL -> "재학중";
        };
    }

    public static StudentStatus fromKorean(String value) {
        return switch (value) {
            case "취업" -> EMPLOY;
            case "중도탈락" -> DROPOUT;
            case "재학중" -> ENROLL;
            default -> throw new IllegalArgumentException("지원하지 않는 학생 상태입니다: " + value);
        };
    }
}
