package shop.mtcoding.blog.web.paper;

import lombok.Data;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.Paper;
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
        private Integer questionPoint;
        private Integer questionAnswerNumber;
        private String questionPurpose;
        private String questionFail;
        List<OptionDTO> options;

        public Question toEntity(Paper paper, SubjectElement subjectElement) {
            return Question.builder()
                    .no(questionNo)
                    .title(questionTitle)
                    .point(questionPoint)
                    .answerNumber(questionAnswerNumber)
                    .questionPurpose(questionPurpose)
                    .questionFail(questionFail)
                    .paper(paper)
                    .subjectElement(subjectElement)
                    .build();
        }

        @Data
        public static class OptionDTO {
            private Integer optionNo;
            private String optionContent;
            private Boolean optionRight;

            public QuestionOption toEntity(Question question) {
                return QuestionOption.builder()
                        .no(optionNo)
                        .content(optionContent)
                        .isRight(optionRight)
                        .question(question)
                        .build();
            }
        }
    }

    @Data
    public static class SaveDTO {
        private Integer count;
        private String paperState; // 평가 // 재평가
        private LocalDate evaluationDate;
        private String evaluationWay;

        public Paper toEntity(Subject subject) {
            return Paper.builder()
                    .subject(subject)
                    .count(count)
                    .paperState(paperState)
                    .evaluationDate(evaluationDate)
                    .evaluationWay(evaluationWay)
                    .build();
        }
    }
}
