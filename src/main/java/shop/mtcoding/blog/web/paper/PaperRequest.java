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

public class PaperRequest {

    @Data
    public static class QuestionSaveDTO {
        private Long elementId;
        private Long paperId;
        private Integer questionNo;
        private String questionTitle;
        private String stimulusFileBase64; // ← 여기로 base64 받음
        private List<OptionDTO> options;

        public Question toEntity(Paper paper, SubjectElement element, String imagePath) {
            return Question.builder()
                    .no(questionNo)
                    .title(questionTitle)
                    .paper(paper)
                    .subjectElement(element)
                    .stimulusImg(imagePath) // 저장된 경로
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
                        .rubricItem(rubricItem)
                        .question(question)
                        .build();
            }
        }
    }

    @Data
    public static class SaveDTO {
        private Integer questionCount;
        private PaperType paperType; // 본평가 / 재평가
        private LocalDate evaluationDate;
        private EvaluationWay evaluationWay; // 서술형 / 혼합형 / 포트폴리오

        public Paper toEntity(Subject subject) {
            return Paper.builder()
                    .subject(subject)
                    .questionCount(questionCount)
                    .paperType(paperType)
                    .evaluationDate(evaluationDate)
                    .evaluationWay(evaluationWay)
                    .build();
        }
    }

}
