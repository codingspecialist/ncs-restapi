package shop.mtcoding.blog.domain.course.subject.paper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception400;
import shop.mtcoding.blog.core.errors.exception.Exception404;
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

        if (!reqDTO.getEvaluationWay().equals(EvaluationWay.MCQ)) {
            if (reqDTO.getGuideSummary() == null || reqDTO.getGuideSummary().isBlank()) {
                throw new IllegalArgumentException("객관식이 아닌 경우, guideSummary는 필수입니다.");
            }
        }

        if (!reqDTO.getEvaluationWay().equals(EvaluationWay.MCQ)) {
            if (reqDTO.getSubmissionFormat() == null || reqDTO.getSubmissionFormat().isBlank()) {
                throw new IllegalArgumentException("객관식이 아닌 경우, 제출자료 안내는 필수입니다.");
            }
        }

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
                .orElseThrow(() -> new Exception404("능력단위 요소가 존재하지 않아요"));

        EvaluationWay evalWay = paper.getEvaluationWay();

        // 공통 유효성 검사
        if (reqDTO.getQuestionNo() == null || reqDTO.getQuestionNo() <= 0) {
            throw new Exception400("문제 번호는 필수입니다.");
        }
        if (reqDTO.getQuestionTitle() == null || reqDTO.getQuestionTitle().isBlank()) {
            throw new Exception400("문제 제목은 필수입니다.");
        }

        String imgPath = null;

        // ✅ 객관식일 때
        if (evalWay == EvaluationWay.MCQ) {
            // 이미지 처리 (선택)
            if (reqDTO.getStimulusFileBase64() != null && !reqDTO.getStimulusFileBase64().isBlank()) {
                imgPath = MyUtil.fileWrite(reqDTO.getStimulusFileBase64());
            }

            // 정답은 반드시 하나
            long correctCount = reqDTO.getOptions().stream()
                    .filter(opt -> opt.getOptionPoint() != null && opt.getOptionPoint() == 1)
                    .count();

            if (correctCount != 1) {
                throw new Exception400("객관식 문제는 정답이 반드시 1개여야 합니다.");
            }

            // 정답에는 루브릭 설명 필수
            for (var opt : reqDTO.getOptions()) {
                if (opt.getOptionPoint() != null && opt.getOptionPoint() == 1) {
                    if (opt.getRubricItem() == null || opt.getRubricItem().isBlank()) {
                        throw new Exception400("정답의 루브릭 설명은 필수입니다.");
                    }
                }
            }
        }
        // ✅ 서술형/작업형/프로젝트형일 때
        else {
            if (reqDTO.getScenario() == null || reqDTO.getScenario().isBlank()) {
                throw new Exception400("문제 시나리오는 필수입니다.");
            }

            // 옵션마다 루브릭과 점수 체크
            for (var opt : reqDTO.getOptions()) {
                if (opt.getRubricItem() == null || opt.getRubricItem().isBlank()) {
                    throw new Exception400("루브릭 항목 설명은 필수입니다.");
                }
                if (opt.getOptionPoint() == null || opt.getOptionPoint() <= 0) {
                    throw new Exception400("루브릭 점수는 1 이상이어야 합니다.");
                }
            }
        }

        // 저장
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
