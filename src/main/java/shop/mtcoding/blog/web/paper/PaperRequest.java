package shop.mtcoding.blog.web.paper;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.EvaluationWay;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
import shop.mtcoding.blog.domain.course.subject.paper.PaperType;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PaperRequest {
    
    @Data
    public class QuestionSaveDTO {
        private Long elementId;
        private Long paperId;
        private Integer questionNo;
        private String questionTitle;

        // 객관식일 경우 사용
        private String stimulusFileBase64;

        // 서술형일 경우 사용
        private String scenario;
        private String scenarioLink;

        private List<OptionDTO> options;

        public Question toEntity(Paper paper, SubjectElement element, String imagePath) {
            return Question.builder()
                    .no(questionNo)
                    .title(questionTitle)
                    .stimulusImg(Optional.ofNullable(imagePath).filter(s -> !s.isBlank()).orElse(null))
                    .scenario(Optional.ofNullable(scenario).filter(s -> !s.isBlank()).orElse(null))
                    .scenarioLink(Optional.ofNullable(scenarioLink).filter(s -> !s.isBlank()).orElse(null))
                    .paper(paper)
                    .subjectElement(element)
                    .build();
        }

        @Data
        public static class OptionDTO {
            private Integer optionNo;
            private String optionContent;
            private Integer optionPoint;
            private String rubricItem;

            public QuestionOption toEntity(Question question) {
                return QuestionOption.builder()
                        .no(optionNo)
                        .content(optionContent)
                        .point(optionPoint)
                        .rubricItem(Optional.ofNullable(rubricItem).filter(s -> !s.isBlank()).orElse(null))
                        .question(question)
                        .build();
            }
        }
    }

    @Data
    public static class SaveDTO {
        private PaperType paperType; // 본평가 / 재평가
        private LocalDate evaluationDate;
        private EvaluationWay evaluationWay; // 서술형 / 혼합형 / 포트폴리오

        private String evaluationRoom; // 평가 장소
        private String evaluationDevice; // 평가 장비
        private Double scorePolicy; // 점수 환산 비율

        private String guideSummary; // 훈련생용 안내 (객관식 제외 시 필수)
        private String guideLink;    // 강사용 외부 링크
        private String submissionFormat;

        public Paper toEntity(Subject subject) {
            return Paper.builder()
                    .subject(subject)
                    .paperType(paperType)
                    .evaluationDate(evaluationDate)
                    .evaluationWay(evaluationWay)
                    .evaluationRoom(evaluationRoom)
                    .evaluationDevice(evaluationDevice)
                    .scorePolicy(scorePolicy)
                    .guideSummary(guideSummary)
                    .guideLink(guideLink)
                    .submissionFormat(submissionFormat)
                    .build();
        }
    }


}
