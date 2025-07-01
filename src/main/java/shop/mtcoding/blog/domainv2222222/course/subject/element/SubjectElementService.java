package shop.mtcoding.blog.domainv2222222.course.subject.element;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.domainv2222222.course.subject.Subject;
import shop.mtcoding.blog.domainv2222222.course.subject.SubjectRepository;
import shop.mtcoding.blog.webv2.course.subject.element.SubjectElementRequest;

import java.util.HashSet;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectElementService {
    private final SubjectRepository subjectRepository;
    private final SubjectElementRepository subjectElementRepository;

    public SubjectElementModel.Items 교과목요소목록(Long subjectId) {

        Subject subjectPS = subjectRepository.findByIdWithElements(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        return new SubjectElementModel.Items(subjectPS);
    }

    @Transactional
    public void 교과목요소전체등록(Long subjectId, List<SubjectElementRequest.Save> reqDTOs) {
        // 1. 교과목 존재확인
        Subject subjectPS = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        // 2. 중복 순번 조회 (한 번의 쿼리로!)
        List<Integer> requestedNos = reqDTOs.stream()
                .map(SubjectElementRequest.Save::getNo)
                .toList();

        List<Integer> existingNos = subjectElementRepository.findNosBySubjectIdAndNoIn(subjectId, requestedNos);

        if (!existingNos.isEmpty()) {
            throw new Exception400("이미 존재하는 순번: " + new HashSet<>(existingNos));
        }

        // 3. 교과목 요소 저장
        List<SubjectElement> subjectElements = reqDTOs.stream().map(saveDTO -> saveDTO.toEntity(subjectPS)).toList();
        subjectElementRepository.saveAll(subjectElements);
    }
}
