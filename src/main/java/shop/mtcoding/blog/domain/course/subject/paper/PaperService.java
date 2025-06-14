package shop.mtcoding.blog.domain.course.subject.paper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception400;
import shop.mtcoding.blog.core.errors.exception.Exception404;
import shop.mtcoding.blog.core.errors.exception.api.ApiException404;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionQueryRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOptionRepository;
import shop.mtcoding.blog.web.paper.PaperRequest;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PaperService {
    private final PaperRepository paperRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionQueryRepository questionQueryRepository;
    private final SubjectElementRepository subjectElementRepository;
    private final SubjectRepository subjectRepository;

    // 교과목별 시험지 목록
    public PaperModel.Items 교과목별시험지목록(Long subjectId) {
        List<Paper> papers = paperRepository.findAllBySubjectId(subjectId);
        return new PaperModel.Items(papers);
    }

    // 시험지 상세
    public PaperModel.Detail 시험지상세(Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        List<Question> questionListPS = questionRepository.findAllByPaperId(paperId);
        return new PaperModel.Detail(paperPS, questionListPS);
    }

    @Transactional
    public void 시험지등록(Long subjectId, PaperRequest.SaveDTO reqDTO) {
        Subject subjectPS = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없어요"));

        // ORIGINAL 유형이면 해당 교과목에 이미 존재하는지 확인
        if (reqDTO.getPaperType() == PaperType.ORIGINAL) {
            boolean exists = paperRepository.existsBySubjectIdAndPaperType(subjectId, PaperType.ORIGINAL);
            if (exists) {
                throw new Exception400("해당 교과목에는 이미 본평가(ORIGINAL) 시험지가 존재합니다.");
            }
        }

        paperRepository.save(reqDTO.toEntity(subjectPS));
    }

    @Transactional
    public void 문제등록(PaperRequest.QuestionSaveDTO reqDTO) {
        Paper paper = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        SubjectElement subjectElement = subjectElementRepository.findById(reqDTO.getElementId())
                .orElseThrow(() -> new ApiException404("능력단위 요소가 존재하지 않아요"));

        String imgPath = "";
        if (reqDTO.getStimulusFileBase64() != null && !reqDTO.getStimulusFileBase64().isBlank()) {
            imgPath = MyUtil.fileWrite(reqDTO.getStimulusFileBase64());
        }

        Question question = questionRepository.save(reqDTO.toEntity(paper, subjectElement, imgPath));
        List<QuestionOption> options = reqDTO.getOptions().stream()
                .map(opt -> opt.toEntity(question))
                .toList();
        questionOptionRepository.saveAll(options);
    }


    public PaperModel.NextQuestion 다음문제준비(Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        List<SubjectElement> elementListPS = subjectElementRepository.findAllBySubjectId(paperPS.getSubject().getId());

        PaperModel.NextQuestion nextQuestion = questionQueryRepository.findNextNo(paperId)
                .withElements(elementListPS, paperPS);

        return nextQuestion;
    }

}
