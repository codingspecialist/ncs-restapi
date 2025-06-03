package shop.mtcoding.blog.domain.course.subject.paper;

public enum EvaluationWay {
    DESCRIPTIVE,  // 서술형
    MIXED,        // 혼합형
    PORTFOLIO;    // 포트폴리오

    public String toKorean() {
        return switch (this) {
            case DESCRIPTIVE -> "서술형";
            case MIXED -> "혼합형";
            case PORTFOLIO -> "포트폴리오";
        };
    }

    public static EvaluationWay fromKorean(String value) {
        return switch (value) {
            case "서술형" -> DESCRIPTIVE;
            case "혼합형" -> MIXED;
            case "포트폴리오" -> PORTFOLIO;
            default -> throw new IllegalArgumentException("지원하지 않는 평가 방식입니다: " + value);
        };
    }
}