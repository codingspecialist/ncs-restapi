package shop.mtcoding.blog.exam.web.dto;

import lombok.Data;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;

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

        @Data
        public static class OptionDTO {
            private Integer optionNo;
            private String optionContent;
            private Integer optionPoint;

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

    }
}