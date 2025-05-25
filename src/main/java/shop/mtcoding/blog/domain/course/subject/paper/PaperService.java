package shop.mtcoding.blog.domain.course.subject.paper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception404;
import shop.mtcoding.blog.core.errors.exception.api.ApiException400;
import shop.mtcoding.blog.core.errors.exception.api.ApiException404;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElement;
import shop.mtcoding.blog.domain.course.subject.element.SubjectElementRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.Question;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionDBResponse;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionQueryRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.QuestionRepository;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOption;
import shop.mtcoding.blog.domain.course.subject.paper.question.option.QuestionOptionRepository;

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

    // 전체 시험지 목록
    public PaperResponse.PagingDTO 시험지목록(Pageable pageable) {
        Page<Paper> paperPG = paperRepository.findAll(pageable);
        return new PaperResponse.PagingDTO(paperPG);
    }

    public PaperResponse.QuestionListDTO 문제목록(Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        List<SubjectElement> subjectElementListPS =
                subjectElementRepository.findBySubjectId(paperPS.getSubject().getId());


        List<Question> questionListPS = questionRepository.findByPaperId(paperId);
        return new PaperResponse.QuestionListDTO(paperPS, subjectElementListPS, questionListPS);
    }

    @Transactional
    public void 시험지등록(PaperRequest.SaveDTO reqDTO) {
        Subject subjectPS = subjectRepository.findById(reqDTO.getSubjectId())
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없어요"));
        paperRepository.save(reqDTO.toEntity(subjectPS));
    }

    @Transactional
    public void 문제등록(PaperRequest.QuestionSaveDTO reqDTO) {
        Paper paperPS = paperRepository.findById(reqDTO.getPaperId())
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

        // 번호 유니크 계산
        List<Question> questionListPS = questionRepository.findByPaperId(reqDTO.getPaperId());
        questionListPS.forEach(question -> {
            if (question.getNo() == reqDTO.getQuestionNo()) {
                throw new ApiException400("동일한 문제 번호를 등록할 수 없어요");
            }
        });

        // 총점 계산
        int prevSum = questionListPS.stream().mapToInt(question -> question.getPoint()).sum();
        if (prevSum + reqDTO.getQuestionPoint() > 100) {
            throw new ApiException400("점수 합계가 100점을 넘을 수 없어요");
        }

        SubjectElement subjectElement = subjectElementRepository.findById(reqDTO.getElementId())
                .orElseThrow(() -> new ApiException404("능력단위 요소가 존재하지 않아요"));

        Question questionPS = questionRepository.save(reqDTO.toEntity(paperPS, subjectElement));
        List<QuestionOption> optionList = reqDTO.getOptions().stream().map(optionDTO -> optionDTO.toEntity(questionPS)).toList();
        questionOptionRepository.saveAll(optionList);
    }

    public QuestionDBResponse.ExpectedNextDTO 다음예상문제(Long paperId) {
        Paper paperPS = paperRepository.findById(paperId)
                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));


        List<SubjectElement> elementListPS = subjectElementRepository.findBySubjectId(paperPS.getSubject().getId());

        QuestionDBResponse.ExpectedNextDTO respDTO = questionQueryRepository.findStatisticsByPaperId(paperId);

        respDTO.setElements(elementListPS);

        return respDTO;
    }
}
