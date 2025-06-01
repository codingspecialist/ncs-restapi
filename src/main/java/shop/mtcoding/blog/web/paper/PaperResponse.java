package shop.mtcoding.blog.web.paper;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;

import java.time.LocalDate;
import java.util.List;

public class PaperResponse {

    @Data
    public static class CourseListDTO {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부
        private List<DTO> courses;

        public CourseListDTO(Page<Course> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.courses = paging.getContent().stream().map(CourseListDTO.DTO::new).toList();
        }

        @Data
        class DTO {
            private Long id;
            private String title;
            private String code;
            private Integer totalTime;
            private Integer totalDay;
            private Integer round;
            private LocalDate startDate;
            private LocalDate endDate;
            private String teacherName;
            private String courseStatus;

            public DTO(Course course) {
                this.id = course.getId();
                this.title = course.getTitle();
                this.code = course.getCode();
                this.totalTime = course.getTotalTime();
                this.totalDay = course.getTotalDay();
                this.round = course.getRound();
                this.startDate = course.getStartDate();
                this.endDate = course.getEndDate();
                this.teacherName = course.getMainTeacherName();
                this.courseStatus = course.getCourseStatus().getValue();
            }
        }
    }

    @Data
    public static class SubjectDTO {
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
        private String evaluationDate; // 평가일
        private String revaluationDate; // 재평가일
        private LocalDate startDate; // 교과목 시작 날짜
        private LocalDate endDate; // 교과목 종료 날짜
        private Long courseId; // 과정 PK
        private String courseTitle;
        private Integer courseRound;

        public SubjectDTO(Subject subject) {
            Paper paper = subject.getPapers().stream().filter(p -> p.getIsReEvaluation() == false).findFirst().orElse(null);
            Paper rePaper = subject.getPapers().stream().filter(p -> p.getIsReEvaluation() == true).findFirst().orElse(null);

            this.subjectId = subject.getId();
            this.code = subject.getCode();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.gubun = subject.getGubun();
            this.grade = subject.getGrade();
            this.totalTime = subject.getTotalTime();
            this.no = subject.getNo();
            this.learningWay = subject.getLearningWay();
            this.evaluationWay = paper.getEvaluationWay();
            this.evaluationDate = paper.getEvaluationDate().toString();

            if (rePaper != null) {
                this.revaluationDate = rePaper.getEvaluationDate().toString();
            } else {
                this.revaluationDate = "시험지없음";
            }
            this.startDate = subject.getStartDate();
            this.endDate = subject.getEndDate();
            this.courseId = subject.getCourse().getId();
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
        }
    }


    @Data
    public static class QuestionListDTO {
        private Long paperId;
        private String evaluationDate; // 평가일 (subject)
        private String loc; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private List<String> subjectElements;
        private List<QuestionDTO> questions;

        public QuestionListDTO(Paper paper, List<SubjectElement> subjectElements, List<Question> questions) {
            this.paperId = paper.getId();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.loc = "3호";
            this.subjectTitle = paper.getSubject().getTitle();
            this.subjectElements = subjectElements.stream().map(se -> se.getSubtitle()).toList();
            this.questions = questions.stream().map(QuestionDTO::new).toList();
        }

        @Data
        class QuestionDTO {
            private Long questionId;
            private Integer no;
            private String title;
            private List<OptionDTO> options;

            public QuestionDTO(Question question) {
                this.questionId = question.getId();
                this.no = question.getNo();
                this.title = question.getTitle();
                this.options = question.getQuestionOptions().stream().map(OptionDTO::new).toList();
            }

            @Data
            class OptionDTO {
                private Long optionId;
                private Integer no;
                private String content;
                private Boolean isRight;

                public OptionDTO(QuestionOption option) {
                    this.optionId = option.getId();
                    this.no = option.getNo();
                    this.content = option.getContent();
                    this.isRight = option.getIsRight();
                }
            }
        }
    }


    @Data
    public static class ListDTO {
        private Integer totalPage; // 전체 페이지 수
        private Integer pageSize; // 페이지 별 아이템 개수
        private Integer pageNumber; // 현재 페이지 번호
        private Boolean isFirst; // 첫번째 페이지 여부
        private Boolean isLast; // 마지막 페이지 여부
        private List<PaperDTO> papers;

        public ListDTO(Page<Paper> paging) {
            this.totalPage = paging.getTotalPages();
            this.pageSize = paging.getSize();
            this.pageNumber = paging.getNumber();
            this.isFirst = paging.isFirst();
            this.isLast = paging.isLast();
            this.papers = paging.getContent().stream().map(PaperDTO::new).toList();
        }

        @Data
        class PaperDTO {
            private Long paperId;

            private String courseTitle;
            private Integer courseRound;
            private Long subjectId;
            private String subjectTitle; // 교과목명
            private Integer count; // 문항수
            private String paperState;

            public PaperDTO(Paper paper) {
                this.paperId = paper.getId();
                this.courseTitle = paper.getSubject().getCourse().getTitle();
                this.courseRound = paper.getSubject().getCourse().getRound();
                this.subjectId = paper.getSubject().getId();
                this.subjectTitle = paper.getSubject().getTitle();
                this.count = paper.getCount();
                this.paperState = paper.getPaperState();
            }
        }
    }
}
