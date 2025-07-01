package shop.mtcoding.blog.domainv2222222.course.exam;

public enum ExamNotTakenReason {
    ABSENT("결석"),
    LATE("지각"),
    SICK_LEAVE("병가"),
    JOB_INTERVIEW("면접"),
    VACATION("휴가"),
    PERSONAL_REASON("개인 사정");

    private final String korean;

    ExamNotTakenReason(String korean) {
        this.korean = korean;
    }

    public String toKorean() {
        return korean;
    }

    public static ExamNotTakenReason fromKorean(String korean) {
        for (ExamNotTakenReason reason : values()) {
            if (reason.korean.equals(korean)) {
                return reason;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 미응시 사유입니다: " + korean);
    }
}
