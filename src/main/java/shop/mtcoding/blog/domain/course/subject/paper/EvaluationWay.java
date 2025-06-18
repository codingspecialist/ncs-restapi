package shop.mtcoding.blog.domain.course.subject.paper;

public enum EvaluationWay {
    MCQ,     // 객관식
    PRACTICAL,     // 작업형
    PROJECT;     // 프로젝트형(PBL)

    public String toKorean() {
        return switch (this) {
            case MCQ -> "객관식";
            case PRACTICAL -> "작업형";
            case PROJECT -> "프로젝트형(PBL)";
        };
    }

    public static EvaluationWay fromKorean(String value) {
        return switch (value) {
            case "객관식" -> MCQ;
            case "작업형" -> PRACTICAL;
            case "프로젝트형(PBL)" -> PROJECT;
            default -> throw new IllegalArgumentException("지원하지 않는 평가 방식입니다: " + value);
        };
    }

    public static EvaluationWay fromStringCode(String value) {
        try {
            return EvaluationWay.valueOf(value.toUpperCase()); // 대소문자 방지
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("지원하지 않는 평가 코드입니다: " + value);
        }
    }
}
