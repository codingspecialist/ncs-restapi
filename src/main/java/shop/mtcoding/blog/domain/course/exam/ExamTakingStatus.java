package shop.mtcoding.blog.domain.course.exam;

/**
 * 학생 입장에서의 시험지 응시 상태
 */
public enum ExamTakingStatus {
    TAKEN,         // 이미 응시 완료한 상태
    AVAILABLE,     // 지금 응시 가능한 상태
    NOT_AVAILABLE  // 응시 자격이 안되는 상태
}