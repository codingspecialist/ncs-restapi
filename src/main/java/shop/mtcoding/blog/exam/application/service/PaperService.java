package shop.mtcoding.blog.exam.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.exam.adapter.CourseRepositoryAdapterInExam;
import shop.mtcoding.blog.exam.application.domain.Paper;
import shop.mtcoding.blog.exam.application.domain.Question;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;
import shop.mtcoding.blog.exam.application.domain.enums.PaperVersion;
import shop.mtcoding.blog.exam.application.repository.PaperRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionOptionRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionQueryRepository;
import shop.mtcoding.blog.exam.application.repository.QuestionRepository;
import shop.mtcoding.blog.exam.web.dto.PaperRequest;
import shop.mtcoding.blog.exam.web.dto.PaperResponse;

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
        private final CourseRepositoryAdapterInExam courseRepositoryAdapter;

        // 교과목별 시험지 목록
        public PaperResponse.MaxList 교과목별시험지목록(Long subjectId) {
                List<Paper> papers = paperRepository.findAllBySubjectId(subjectId);
                return new PaperResponse.MaxList(papers);
        }

        // 시험지 상세
        public PaperResponse.Detail 시험지상세(Long paperId) {
                Paper paperPS = paperRepository.findById(paperId)
                                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

                List<Question> questionListPS = questionRepository.findAllByPaperId(paperId);
                return new PaperResponse.Detail(paperPS, questionListPS);
        }

        @Transactional
        public void 시험지등록(PaperRequest.Save request) {
                Subject subjectPS = courseRepositoryAdapter.findSubjectById(request.getSubjectId())
                                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없어요"));

                // ORIGINAL 유형이면 해당 교과목에 이미 존재하는지 확인
                if (request.getPaperVersion() == PaperVersion.ORIGINAL) {
                        boolean exists = paperRepository.existsBySubjectIdAndPaperVersion(subjectPS.getId(),
                                        PaperVersion.ORIGINAL);
                        if (exists) {
                                throw new Exception404("해당 교과목에는 이미 본평가(ORIGINAL) 시험지가 존재합니다.");
                        }
                }

                Paper paper = Paper.builder()
                                .subject(subjectPS)
                                .paperVersion(request.getPaperVersion())
                                .evaluationDate(request.getEvaluationDate())
                                .evaluationWay(request.getEvaluationWay())
                                .evaluationRoom(request.getEvaluationRoom())
                                .evaluationDevice(request.getEvaluationDevice())
                                .taskTitle(request.getTaskTitle())
                                .taskScenario(request.getTaskScenario())
                                .taskScenarioGuideLink(request.getTaskScenarioGuideLink())
                                .taskSubmitFormat(request.getTaskSubmitFormat())
                                .taskSubmitTemplateLink(request.getTaskSubmitTemplateLink())
                                .taskChallenge(request.getTaskChallenge())
                                .build();

                paperRepository.save(paper);
        }

        @Transactional
        public void 문제등록(Long paperId, PaperRequest.QuestionSave request) {
                Paper paper = paperRepository.findById(paperId)
                                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

                SubjectElement subjectElement = courseRepositoryAdapter.findSubjectElementById(request.getElementId())
                                .orElseThrow(() -> new Exception404("능력단위 요소가 존재하지 않아요"));

                // 저장
                Question question = Question.builder()
                                .no(request.getQuestionNo())
                                .title(request.getQuestionTitle())
                                .summary(request.getSummary())
                                .paper(paper)
                                .subjectElement(subjectElement)
                                .build();

                Question questionPS = questionRepository.save(question);

                List<QuestionOption> options = request.getOptions().stream()
                                .map(opt -> QuestionOption.builder()
                                                .no(opt.getOptionNo())
                                                .content(opt.getOptionContent() != null
                                                                && !opt.getOptionContent().isBlank()
                                                                                ? opt.getOptionContent()
                                                                                : null)
                                                .point(opt.getOptionPoint() != null ? opt.getOptionPoint() : 0)
                                                .question(questionPS)
                                                .build())
                                .toList();
                questionOptionRepository.saveAll(options);
        }

        public PaperResponse.NextQuestion 다음문제준비(Long paperId) {
                Paper paperPS = paperRepository.findById(paperId)
                                .orElseThrow(() -> new Exception404("시험지가 존재하지 않아요"));

                List<SubjectElement> elementListPS = courseRepositoryAdapter
                                .findAllBySubjectId(paperPS.getSubject().getId());

                PaperResponse.NextQuestion nextQuestion = questionQueryRepository.findNextNo(paperId)
                                .withElements(elementListPS, paperPS);

                return nextQuestion;
        }

}
