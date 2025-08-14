package shop.mtcoding.blog.exam.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.course.application.repository.SubjectElementRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.repository.PaperRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionOptionRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionQueryRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionRepository;
import shop.mtcoding.blog.exam.application.service.dto.PaperCommand;
import shop.mtcoding.blog.exam.application.service.dto.PaperOutput;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PaperService {
    private final PaperRepository paperRepository;
    private final QuestionRepository questionRepository;
    private final QuestionQueryRepository questionQueryRepository;
    private final QuestionOptionRepository questionOptionRepository;

    // 어뎁터로 가져와야함
    private final SubjectElementRepository subjectElementRepository;
    private final SubjectRepository subjectRepository;

    // 교과목별 시험지 목록
    public PaperOutput.MaxList 교과목별시험지목록(Long subjectId) {
        List<Paper> papers = paperRepository.findAllBySubjectId(subjectId);
        return new PaperOutput.MaxList(papers);
    }

    // 시험지 상세
    public PaperOutput.Detail 시험지상세(Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        List<Question> questionListPS = questionRepository.findAllByPaperId(paperId);
        return new PaperOutput.Detail(paperPS, questionListPS);
    }

    @Transactional
    public void 시험지등록(PaperCommand.Save command) {
        Subject subjectPS = subjectRepository.findById(command.getSubjectId())
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없어요"));

        // ORIGINAL 유형이면 해당 교과목에 이미 존재하는지 확인
        if (command.getPaperVersion() == PaperVersion.ORIGINAL) {
            boolean exists = paperRepository.existsBySubjectIdAndPaperVersion(subjectPS.getId(), PaperVersion.ORIGINAL);
            if (exists) {
                throw new Exception404("해당 교과목에는 이미 본평가(ORIGINAL) 시험지가 존재합니다.");
            }
        }

        paperRepository.save(command.toEntity(subjectPS));
    }

    @Transactional
    public void 문제등록(Long paperId, PaperCommand.QuestionSave command) {
        Paper paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        SubjectElement subjectElement = subjectElementRepository.findById(command.getElementId())
                .orElseThrow(() -> new Exception404("능력단위 요소가 존재하지 않아요"));

        // 저장
        Question question = questionRepository.save(command.toEntity(paper, subjectElement));
        List<QuestionOption> options = command.getOptions().stream()
                .map(opt -> opt.toEntity(question))
                .toList();
        questionOptionRepository.saveAll(options);
    }


    public PaperOutput.NextQuestion 다음문제준비(Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        List<SubjectElement> elementListPS = subjectElementRepository.findAllBySubjectId(paperPS.getSubject().getId());

        PaperOutput.NextQuestion nextQuestion = questionQueryRepository.findNextNo(paperId)
                .withElements(elementListPS, paperPS);

        return nextQuestion;
    }

}
