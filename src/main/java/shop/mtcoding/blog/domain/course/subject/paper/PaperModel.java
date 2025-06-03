package shop.mtcoding.blog.domain.course.subject.paper;

import org.springframework.data.domain.Page;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;

import java.util.List;


public class PaperModel {
    public record Items(Page<Paper> paperPG) {
    }

    public record Detail(Paper paper, List<SubjectElement> subjectElements, List<Question> questions) {
    }

    public record NextQuestion(Integer expectNo, Integer expectPoint, Long paperId, List<Element> elements) {
        public NextQuestion(Integer expectNo, Integer expectPoint, Long paperId) {
            this(expectNo, expectPoint, paperId, List.of());
        }

        public NextQuestion withElements(List<SubjectElement> subjectElements) {
            List<Element> converted = subjectElements.stream()
                    .map(e -> new Element(e.getId(), e.getSubtitle()))
                    .toList();
            return new NextQuestion(expectNo, expectPoint, paperId, converted);
        }

        record Element(Long elementId, String subtitle) {
        }
    }
}
