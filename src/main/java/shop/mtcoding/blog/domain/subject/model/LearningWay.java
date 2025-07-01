package shop.mtcoding.blog.domain.subject.model;

public enum LearningWay {
    THEORY,
    PRACTICE,
    MIXED;

    public String toKorean() {
        return switch (this) {
            case THEORY -> "이론";
            case PRACTICE -> "실습";
            case MIXED -> "이론+실습";
        };
    }

    public static LearningWay fromKorean(String value) {
        return switch (value) {
            case "이론" -> THEORY;
            case "실습" -> PRACTICE;
            case "이론+실습" -> MIXED;
            default -> throw new IllegalArgumentException("지원하지 않는 학습 방법입니다: " + value);
        };
    }
}
