package shop.mtcoding.blog.web.paper;

import lombok.Data;
import org.springframework.data.domain.Page;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionOption;

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
                this.courseStatus = course.getCourseStatus().toKorean();
            }
        }
    }

    @Data
    public static class SubjectDTO {
        private Long subjectId;
        private Integer no; // 과정에서 몇번째로 시작하는 교과목인지에 대한 연번
        private String code; // 능력단위 코드
        private String title;
        private String purpose;
        private String ncsType;
        private Integer grade;
        private Integer totalTime;
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
            Paper paper = subject.getPapers().stream().filter(p -> !p.isReTest()).findFirst().orElse(null);
            Paper rePaper = subject.getPapers().stream().filter(p -> p.isReTest()).findFirst().orElse(null);

            this.subjectId = subject.getId();
            this.code = subject.getCode();
            this.title = subject.getTitle();
            this.purpose = subject.getPurpose();
            this.ncsType = subject.getNcsType().toKorean();
            this.grade = subject.getGrade();
            this.totalTime = subject.getTotalTime();
            this.no = subject.getNo();
            this.learningWay = subject.getLearningWay().toKorean();
            if (paper != null) {
                this.evaluationWay = paper.getEvaluationWay() != null ? paper.getEvaluationWay().toKorean() : "시험지없음"; // ✅ enum 변경
                this.evaluationDate = paper.getEvaluationDate() != null ? paper.getEvaluationDate().toString() : "시험지없음";
            } else {
                this.evaluationWay = "시험지없음";
                this.evaluationDate = "시험지없음";
            }

            this.revaluationDate = (rePaper != null && rePaper.getEvaluationDate() != null)
                    ? rePaper.getEvaluationDate().toString()
                    : "시험지없음";

            this.startDate = subject.getStartDate();
            this.endDate = subject.getEndDate();
            this.courseId = subject.getCourse().getId();
            this.courseTitle = subject.getCourse().getTitle();
            this.courseRound = subject.getCourse().getRound();
        }
    }


    @Data
    public static class McqDetailDTO {
        private Long paperId;
        private String evaluationDate; // 평가일 (subject)
        private String evaluationDevice; // 평가장소 (임시)
        private String evaluationRoom; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)
        private String teacherName;
        private Integer questionCount;
        private List<QuestionItem> questions;

        public McqDetailDTO(Paper paper, List<Question> questions) {
            this.paperId = paper.getId();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.subjectTitle = paper.getSubject().getTitle();
            this.teacherName = paper.getSubject().getTeacher().getName();
            this.questionCount = questions.size();
            this.questions = questions.stream().map(QuestionItem::new).toList();
        }

        @Data
        class QuestionItem {
            private Long questionId;
            private Integer questionNo;
            private String questionTitle;
            private String exContent;
            private Double maxScore; // 만점
            private List<Option> options;

            public QuestionItem(Question question) {
                this.questionId = question.getId();
                this.questionNo = question.getNo();
                this.questionTitle = question.getTitle();
                this.exContent = question.getExContent();
                this.maxScore = question.getPaper().getMaxScore();
                this.options = question.getQuestionOptions().stream().map(Option::new).toList();
            }

            @Data
            class Option {
                private Long optionId;
                private Integer optionNo;
                private String optionContent;
                private Integer optionPoint;
                private Boolean isRight;

                public Option(QuestionOption option) {
                    this.optionId = option.getId();
                    this.optionNo = option.getNo();
                    this.optionContent = option.getContent();
                    this.optionPoint = option.getPoint();
                    this.isRight = option.getPoint() > 0;
                }
            }
        }
    }

    @Data
    public static class RubricDetailDTO {
        private Long paperId;

        private String evaluationDate; // 평가일 (subject)
        private String evaluationRoom; // 평가장소 (임시)
        private String evaluationDevice; // 평가장소 (임시)
        private String subjectTitle; // 교과목 (subject)

        private String teacherName;
        private Integer questionCount;

        // -------------------- 객관식이 아닐때 받아야 할 목록
        private String taskTitle;
        private String taskScenario;
        private String taskScenarioGuideLink;
        private List<String> taskSubmitFormats; // 제출항목 (notion)
        private String taskSubmitTemplateLink; // 제출항목 복제 템플릿 (선택)
        private List<String> taskChallenges; // 도전과제

        private List<QuestionItem> questions;

        public RubricDetailDTO(Paper paper, List<Question> questions) {
            this.paperId = paper.getId();
            this.evaluationDate = paper.getEvaluationDate().toString();
            this.evaluationDevice = paper.getEvaluationDevice();
            this.evaluationRoom = paper.getEvaluationRoom();
            this.subjectTitle = paper.getSubject().getTitle();

            this.teacherName = paper.getSubject().getTeacher().getName();
            this.questionCount = questions.size();

            this.taskTitle = paper.getTaskTitle();
            this.taskScenario = paper.getTaskScenario();
            this.taskScenarioGuideLink = paper.getTaskScenarioGuideLink();
            this.taskSubmitFormats = MyUtil.parseMultilineWithoutHyphen(paper.getTaskSubmitFormat());
            this.taskSubmitTemplateLink = paper.getTaskSubmitTemplateLink();
            this.taskChallenges = MyUtil.parseMultilineWithoutHyphen(paper.getTaskChallenge());
            this.questions = questions.stream().map(QuestionItem::new).toList();
        }

        @Data
        class QuestionItem {
            private Long questionId;
            private Integer questionNo;
            private String questionTitle;
            private List<String> exScenarios; // 가이드 요약본
            private List<Option> options;

            public QuestionItem(Question question) {
                this.questionId = question.getId();
                this.questionNo = question.getNo();
                this.questionTitle = question.getTitle();
                this.exScenarios = MyUtil.parseMultiline(question.getExScenario());
                this.options = question.getQuestionOptions().stream().map(Option::new).toList();
            }

            @Data
            class Option {
                private Long optionId;
                private Integer optionNo;
                private String optionContent;
                private Integer optionPoint;

                public Option(QuestionOption option) {
                    this.optionId = option.getId();
                    this.optionNo = option.getNo();
                    this.optionContent = option.getContent();
                    this.optionPoint = option.getPoint();
                }
            }
        }
    }


    @Data
    public static class DTO {

        private Long paperId;
        private String courseTitle;
        private Integer courseRound;
        private Long subjectId;
        private String subjectTitle; // 교과목명
        private Integer questionCount; // 문항수
        private String paperType;
        private String evaluationWay;
        private String evaluationDate;

        public DTO(Paper paper) {
            this.paperId = paper.getId();
            this.courseTitle = paper.getSubject().getCourse().getTitle();
            this.courseRound = paper.getSubject().getCourse().getRound();
            this.subjectId = paper.getSubject().getId();
            this.subjectTitle = paper.getSubject().getTitle();
            this.questionCount = paper.getQuestions().size();
            this.paperType = paper.getPaperType().toKorean();
            this.evaluationWay = paper.getEvaluationWay().toKorean();
            this.evaluationDate = MyUtil.localDateToString(paper.getEvaluationDate());
        }
    }
}
