package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;

import java.util.List;

public class PaperResponse {


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
            //this.subjectTitle = paper.getSubject().getTitle();
            //this.teacherName = paper.getSubject().getTeacher().getName();
            this.questionCount = questions.size();
            this.questions = questions.stream().map(QuestionItem::new).toList();
        }

        @Data
        class QuestionItem {
            private Long questionId;
            private Integer questionNo;
            private String questionTitle;
            private String questionSummary;
            private Double maxScore; // 만점
            private List<Option> options;

            public QuestionItem(Question question) {
                this.questionId = question.getId();
                this.questionNo = question.getNo();
                this.questionTitle = question.getTitle();
                this.questionSummary = question.getSummary();
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
            //this.subjectTitle = paper.getSubject().getTitle();

            //this.teacherName = paper.getSubject().getTeacher().getName();
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
            private List<String> questionSummaries; // 가이드 요약본
            private List<Option> options;

            public QuestionItem(Question question) {
                this.questionId = question.getId();
                this.questionNo = question.getNo();
                this.questionTitle = question.getTitle();
                this.questionSummaries = MyUtil.parseMultiline(question.getSummary());
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
        private String paperVersion;
        private String evaluationWay;
        private String evaluationDate;

        public DTO(Paper paper) {
            this.paperId = paper.getId();
            //this.courseTitle = paper.getSubject().getCourse().getTitle();
            //this.courseRound = paper.getSubject().getCourse().getRound();
            //this.subjectId = paper.getSubject().getId();
            //this.subjectTitle = paper.getSubject().getTitle();
            this.questionCount = paper.getQuestions().size();
            this.paperVersion = paper.getPaperVersion().toKorean();
            this.evaluationWay = paper.getEvaluationWay().toKorean();
            this.evaluationDate = MyUtil.localDateToString(paper.getEvaluationDate());
        }
    }
}
