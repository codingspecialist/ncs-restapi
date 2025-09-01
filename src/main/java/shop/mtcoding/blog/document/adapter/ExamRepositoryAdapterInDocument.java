package shop.mtcoding.blog.document.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.exam.application.domain.Exam;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.repository.ExamRepository;
import shop.mtcoding.blog.exam.application.repository.PaperRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionRepository;

import java.util.List;
import java.util.Optional;

@Component("examRepositoryAdapterInDocument")
@RequiredArgsConstructor
public class ExamRepositoryAdapterInDocument {

    private final ExamRepository examRepository;
    private final PaperRepository paperRepository;
    private final QuestionRepository questionRepository;

    public Optional<Paper> findPaperBySubjectIdAndPaperVersion(Long subjectId, PaperVersion paperVersion) {
        return paperRepository.findBySubjectIdAndPaperVersion(subjectId, paperVersion);
    }

    public List<Question> findQuestionsByPaperId(Long paperId) {
        return questionRepository.findAllByPaperId(paperId);
    }

    public List<Exam> findExamsBySubjectIdAndIsUseOrderByStudentNameAsc(Long subjectId) {
        return examRepository.findBySubjectIdAndIsUseOrderByStudentNameAsc(subjectId);
    }

    public List<Exam> findExamsBySubjectIdAndPaperVersion(Long subjectId, PaperVersion paperVersion) {
        return examRepository.findAllBySubjectIdAndPaperVersion(subjectId, paperVersion);
    }
}
