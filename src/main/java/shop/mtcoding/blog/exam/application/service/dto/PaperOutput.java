package shop.mtcoding.blog.exam.application.service.dto;

import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.enums.EvaluationWay;

import java.util.List;

public class PaperOutput {
    public record MaxList(List<Paper> papers) {
    }

    public record Detail(Paper paper, List<Question> questions) {
    }

    public record NextQuestion(Integer expectNo, Long paperId, EvaluationWay evaluationWay, List<Element> elements) {
        public NextQuestion(Integer expectNo, Long paperId) {
            this(expectNo, paperId, null, List.of());
        }

        public NextQuestion withElements(List<SubjectElement> subjectElements, Paper paper) {
            List<Element> converted = subjectElements.stream()
                    .map(e -> new Element(e.getId(), e.getTitle()))
                    .toList();
            return new NextQuestion(expectNo, paperId, paper.getEvaluationWay(), converted);
        }

        record Element(Long elementId, String subtitle) {
        }
    }
}
