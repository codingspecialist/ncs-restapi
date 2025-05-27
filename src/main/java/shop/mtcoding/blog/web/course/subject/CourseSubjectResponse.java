package shop.mtcoding.blog.web.course.subject;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;

import java.time.LocalDate;
import java.util.List;

public class CourseSubjectResponse {

    @Data
    public static class DTO {
        private Long subjectId;
        private String subjectTitle;
        private String courseTitle;
        private Integer courseRound;

        public DTO(Subject subject) {
            this.subjectId = subject.getId();
            this.subjectTitle = subject.getTitle();
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
        }
    }

    @Data
    public static class PagingDTO {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부

        private List<SubjectDTO> subjects;

        public PagingDTO(Page<Subject> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.subjects = paging.getContent().stream().map(SubjectDTO::new).toList();
        }

        @Data
        class SubjectDTO {
            private Long subjectId;
            private String code; // 능력단위 코드
            private String title;
            private String purpose;
            private String gubun;
            private Integer grade;
            private Integer totalTime;
            private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 연번
            private String learningWay; // 학습 방법
            private String evaluationWay; // 평가 방법
            private LocalDate evaluationDate; // 평가일
            private LocalDate revaluationDate; // 재평가일
            private LocalDate evaluationScheduleDate; // 평가 예정일
            private LocalDate revaluationScheduleDate; // 재평가 예정일
            private LocalDate startDate; // 교과목 시작 날짜
            private LocalDate endDate; // 교과목 종료 날짜
            private Long courseId; // 과정 PK
            private String courseTitle;
            private Integer courseRound;

            public SubjectDTO(Subject subject) {
                Paper paper = subject.getPapers().stream().filter(p -> p.getIsReEvaluation() == false).findFirst().orElse(null);
                this.subjectId = subject.getId();
                this.code = subject.getCode();
                this.title = subject.getTitle();
                this.purpose = subject.getPurpose();
                this.gubun = subject.getGubun();
                this.grade = subject.getGrade();
                this.totalTime = subject.getTotalTime();
                this.no = subject.getNo();
                this.learningWay = subject.getLearningWay();
                // TODO : 재평가일 때문에 터질수 있음
                this.evaluationWay = paper.getEvaluationWay();
                this.evaluationDate = paper.getEvaluationDate();
                this.startDate = subject.getStartDate();
                this.endDate = subject.getEndDate();
                this.courseId = subject.getCourse().getId();
                this.courseTitle = subject.getCourse().getTitle();
                this.courseRound = subject.getCourse().getRound();
            }
        }
    }
}
