package shop.mtcoding.blog.domain.course.subject.paper;

public enum PaperVersion {
    ORIGINAL, // 본평가
    RETEST;   // 재평가

    public String toKorean() {
        return switch (this) {
            case ORIGINAL -> "본평가";
            case RETEST -> "재평가";
        };
    }

    public static PaperVersion fromKorean(String value) {
        return switch (value) {
            case "본평가" -> ORIGINAL;
            case "재평가" -> RETEST;
            default -> throw new IllegalArgumentException("지원하지 않는 평가 종류입니다: " + value);
        };
    }

    public boolean isReTest() {
        return this == RETEST;
    }
}
