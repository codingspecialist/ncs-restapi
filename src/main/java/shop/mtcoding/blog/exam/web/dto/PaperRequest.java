package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.service.dto.PaperCommand;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaperRequest {

    @Data
    public static class QuestionSave {
        private Long elementId;
        private Integer questionNo;
        private String questionTitle;
        private String summary;

        private List<OptionDTO> options;

        public PaperCommand.QuestionSave toCommand() {
            List<PaperCommand.QuestionSave.Option> optionCommands = null;
            if (options != null) {
                optionCommands = options.stream()
                        .map(OptionDTO::toCommand)
                        .collect(Collectors.toList());
            }

            return PaperCommand.QuestionSave.builder()
                    .elementId(elementId)
                    .questionNo(questionNo)
                    .questionTitle(questionTitle)
                    .summary(summary)
                    .options(optionCommands)
                    .build();
        }

        @Data
        public static class OptionDTO {
            private Integer optionNo;
            private String optionContent;
            private Integer optionPoint;

            public PaperCommand.QuestionSave.Option toCommand() {
                return PaperCommand.QuestionSave.Option.builder()
                        .no(optionNo)
                        .content(Optional.ofNullable(optionContent).filter(s -> !s.isBlank()).orElse(null))
                        .point(Optional.ofNullable(optionPoint).orElse(0))
                        .build();
            }
        }
    }

    @Data
    public static class Save {
        private Long subjectId;
        private PaperVersion paperVersion;
        private LocalDate evaluationDate;

        private String evaluationDevice;
        private String evaluationRoom;

        private EvaluationWay evaluationWay;

        private String taskTitle;
        private String taskScenario;
        private String taskScenarioGuideLink;
        private String taskSubmitFormat;
        private String taskSubmitTemplateLink;
        private String taskChallenge;

        public PaperCommand.Save toCommand() {
            return PaperCommand.Save.builder()
                    .subjectId(subjectId)
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
}