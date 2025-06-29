package shop.mtcoding.blog.web.paper;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperType;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionOption;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaperRequest {

    @Data
    public static class QuestionSaveDTO {
        private Long elementId;
        private Long paperId;
        private Integer questionNo;
        private String questionTitle;

        // 둘 중 택일로 받음
        private String exContent;
        private String exScenario;

        private List<OptionDTO> options;

        public Question toEntity(Paper paper, SubjectElement element) {
            return Question.builder()
                    .no(questionNo)
                    .title(questionTitle)
                    .exContent(exContent)
                    .exScenario(Optional.ofNullable(exScenario).filter(s -> !s.isBlank()).orElse(null))
                    .paper(paper)
                    .subjectElement(element)
                    .build();
        }

        @Data
        public static class OptionDTO {
            private Integer optionNo;
            private String optionContent;
            private Integer optionPoint;

            public QuestionOption toEntity(Question question) {
                return QuestionOption.builder()
                        .no(optionNo)
                        .content(Optional.ofNullable(optionContent).filter(s -> !s.isBlank()).orElse(null))
                        .point(Optional.ofNullable(optionPoint).orElse(0))
                        .question(question)
                        .build();
            }
        }
    }

    @Data
    public static class SaveDTO {
        private PaperType paperType; // 본평가 / 재평가
        private LocalDate evaluationDate;

        private String evaluationDevice; // 평가 장비
        private String evaluationRoom;   // 평가 장소

        private EvaluationWay evaluationWay; // 객관식. 서술형. 작업형. 프로젝트형

        // rubric 관련 항목 (null 가능)
        private String taskTitle;
        private String taskScenario;
        private String taskScenarioGuideLink;
        private String taskSubmitFormat;
        private String taskSubmitTemplateLink;
        private String taskChallenge;

        public Paper toEntity(Subject subject) {
            return Paper.builder()
                    .subject(subject)
                    .paperType(paperType)
                    .evaluationDate(evaluationDate)
                    .evaluationWay(evaluationWay)
                    .evaluationRoom(evaluationRoom)
                    .evaluationDevice(evaluationDevice)
                    .taskTitle(taskTitle)
                    .taskScenario(taskScenario)
                    .taskScenarioGuideLink(taskScenarioGuideLink)
                    .taskSubmitFormat(taskSubmitFormat)
                    .taskSubmitTemplateLink(taskSubmitTemplateLink)
                    .taskChallenge(taskChallenge)
                    .build();
        }
    }

}
