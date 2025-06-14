package shop.mtcoding.blog.domain.course.subject.paper;

public enum EvaluationWay {
    OBJECTIVE,     // 객관식
    DESCRIPTIVE,   // 서술형
    PRACTICAL,     // 작업형
    PROJECT;     // 프로젝트형(PBL)

    public String toKorean() {
        return switch (this) {
            case OBJECTIVE -> "객관식";
            case DESCRIPTIVE -> "서술형";
            case PRACTICAL -> "작업형";
            case PROJECT -> "프로젝트형(PBL)";
        };
    }

    public static EvaluationWay fromKorean(String value) {
        return switch (value) {
            case "객관식" -> OBJECTIVE;
            case "서술형" -> DESCRIPTIVE;
            case "작업형" -> PRACTICAL;
            case "프로젝트형(PBL)" -> PROJECT;
            default -> throw new IllegalArgumentException("지원하지 않는 평가 방식입니다: " + value);
        };
    }
}
