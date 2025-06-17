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
    public static class QuestionSaveDTO {
        private Long elementId;
        private Long paperId;
        private Integer questionNo;
        private String questionTitle;

        // 객관식일 경우 사용
        private String stimulusFileBase64;

        // 객관식이 아닐 경우 사용
        private String scenario;
        // 객관식이 아닐 경우 사용
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
            // 객관식일 경우
            private String optionContent;
            // 객관식의 정답에 체크가 되어있으면 점수를 받고, 아니면 0점을 받기
            private Integer optionPoint;
            // 객관식의 정답에 체크가 되어있으면 루브릭 내용받기
            // 객관식이 아닐경우에는 무조건 받기
            private String rubricItem;

            public QuestionOption toEntity(Question question) {
                return QuestionOption.builder()
                        .no(optionNo)
                        .content(Optional.ofNullable(optionContent).filter(s -> !s.isBlank()).orElse(null))
                        .point(Optional.ofNullable(optionPoint).orElse(0))
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

        private String evaluationDevice; // 평가 장비
        private String evaluationRoom; // 평가 장소

        private EvaluationWay evaluationWay; // 객관식. 서술형. 작업형. 프로젝트형

        private String guideSummary; // 훈련생용 안내 (객관식 제외 시 필수)
        private String guideLink;    // 강사용 외부 링크
        private String submissionFormat;

        public Paper toEntity(Subject subject) {
            // guideLink가 공백이거나 trim() 후 빈 문자열이면 null로 처리
            String processedGuideLink = (this.guideLink != null && !this.guideLink.trim().isEmpty()) ? this.guideLink.trim() : null;

            return Paper.builder()
                    .subject(subject)
                    .paperType(paperType)
                    .evaluationDate(evaluationDate)
                    .evaluationWay(evaluationWay)
                    .evaluationRoom(evaluationRoom)
                    .evaluationDevice(evaluationDevice)
                    .guideSummary(guideSummary)
                    .guideLink(processedGuideLink) // 수정된 guideLink 적용
                    .submissionFormat(submissionFormat)
                    .build();
        }
    }

}
