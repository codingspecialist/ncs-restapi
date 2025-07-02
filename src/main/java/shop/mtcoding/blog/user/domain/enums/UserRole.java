package shop.mtcoding.blog.user.domain.enums;

public enum UserRole {
    STUDENT, // 학생
    TEACHER, // 강사
    EMP;     // 직원 (사원, 팀장, 원장)

    // 프론트 표시용
    public String toKorean() {
        return switch (this) {
            case STUDENT -> "학생";
            case TEACHER -> "강사";
            case EMP -> "직원";
        };
    }

    // DTO 파싱용
    public static UserRole fromKorean(String value) {
        return switch (value) {
            case "학생" -> STUDENT;
            case "강사" -> TEACHER;
            case "직원" -> EMP;
            default -> throw new IllegalArgumentException("지원하지 않는 사용자 유형입니다: " + value);
        };
    }
}
