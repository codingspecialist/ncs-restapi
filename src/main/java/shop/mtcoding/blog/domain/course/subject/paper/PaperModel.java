package shop.mtcoding.blog.domain.course.subject.paper;

import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.util.List;


public class PaperModel {
    public record Items(List<Paper> papers) {
    }

    public record Detail(Paper paper, List<Question> questions) {
    }

    public record NextQuestion(Integer expectNo, Long paperId, String evaluationWay, List<Element> elements) {
        public NextQuestion(Integer expectNo, Long paperId) {
            this(expectNo, paperId, null, List.of());
        }

        public NextQuestion withElements(List<SubjectElement> subjectElements, Paper paper) {
            List<Element> converted = subjectElements.stream()
                    .map(e -> new Element(e.getId(), e.getSubtitle()))
                    .toList();
            return new NextQuestion(expectNo, paperId, paper.getEvaluationWay().toString(), converted);
        }

        record Element(Long elementId, String subtitle) {
        }
    }
}
