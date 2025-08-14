package shop.mtcoding.blog.exam.application.service.dto;

import lombok.Builder;
import lombok.Data;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaperCommand {

    @Builder
    @Data
    public static class Save {
        private Long subjectId;
        private PaperVersion paperVersion; // 본평가 / 재평가
        private LocalDate evaluationDate;

        private String evaluationDevice; // 평가 장비
        private String evaluationRoom;   // 평가 장소

        private EvaluationWay evaluationWay; // 객관식. 서술형. 작업형. 프로젝트형

        private String taskTitle;
        private String taskScenario;
        private String taskScenarioGuideLink;
        private String taskSubmitFormat;
        private String taskSubmitTemplateLink;
        private String taskChallenge;

        // Command에서 Entity로 변환
        public Paper toEntity(Subject subject) {
            return Paper.builder()
                    .subject(subject)
                    .paperVersion(paperVersion)
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

    @Builder
    @Data
    public static class QuestionSave {
        private Long elementId;
        private Integer questionNo;
        private String questionTitle;
        private String summary;

        private List<Option> options;

        // Command에서 Entity로 변환
        public Question toEntity(Paper paper, SubjectElement element) {
            return Question.builder()
                    .no(questionNo)
                    .title(questionTitle)
                    .summary(summary)
                    .paper(paper)
                    .subjectElement(element)
                    .build();
        }

        @Builder
        @Data
        public static class Option {
            private Integer no;
            private String content;
            private Integer point;

            // Command에서 Entity로 변환
            public QuestionOption toEntity(Question question) {
                return QuestionOption.builder()
                        .no(no)
                        .content(Optional.ofNullable(content).filter(s -> !s.isBlank()).orElse(null))
                        .point(Optional.ofNullable(point).orElse(0))
                        .question(question)
                        .build();
            }
        }
    }
}