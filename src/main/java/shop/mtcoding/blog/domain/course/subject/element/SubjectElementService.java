package shop.mtcoding.blog.domain.course.subject.element;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception404;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectElementService {
    private final SubjectRepository subjectRepository;
    private final SubjectElementRepository subjectElementRepository;

    public SubjectElementResponse.ListDTO 교과목요소목록(Long subjectId) {

        Subject subjectPS = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        List<SubjectElement> subjectElementListPS = subjectElementRepository.findBySubjectId(subjectId);
        return new SubjectElementResponse.ListDTO(subjectPS, subjectElementListPS);
    }

    @Transactional
    public void 교과목요소전체등록(Long subjectId, List<SubjectElementRequest.SaveDTO> reqDTOs) {
        Subject subjectPS = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        List<SubjectElement> subjectElements = reqDTOs.stream().map(saveDTO -> saveDTO.toEntity(subjectPS)).toList();
        subjectElementRepository.saveAll(subjectElements);
    }
}
