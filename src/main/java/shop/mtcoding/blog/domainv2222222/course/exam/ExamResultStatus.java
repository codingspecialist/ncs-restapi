package shop.mtcoding.blog.domainv2222222.course.exam;

public enum ExamResultStatus {
    PASS("통과"),
    FAIL("미통과(60점미만)"),
    NOT_TAKEN("미응시"),
    NOT_GRADED("채점전");

    private final String korean;

    ExamResultStatus(String korean) {
        this.korean = korean;
    }

    public String toKorean() {
        return korean;
    }

    public static ExamResultStatus fromKorean(String korean) {
        for (ExamResultStatus state : values()) {
            if (state.korean.equals(korean)) {
                return state;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 상태입니다: " + korean);
    }
}
