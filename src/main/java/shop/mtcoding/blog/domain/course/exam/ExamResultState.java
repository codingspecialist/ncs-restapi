package shop.mtcoding.blog.domain.course.exam;

public enum ExamResultState {
    PASS("통과"),
    FAIL("미통과(60점미만)"),
    ABSENT("결석"),
    NOT_TAKEN("미응시");

    private final String korean;

    ExamResultState(String korean) {
        this.korean = korean;
    }

    public String toKorean() {
        return korean;
    }

    public static ExamResultState fromKorean(String korean) {
        for (ExamResultState state : values()) {
            if (state.korean.equals(korean)) {
                return state;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 상태입니다: " + korean);
    }
}
