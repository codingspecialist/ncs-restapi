package shop.mtcoding.blog.domain.course.subject.element;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception404;
import shop.mtcoding.blog.domain.course.subject.Subject;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;
import shop.mtcoding.blog.web.course.subject.element.CourseSubjectElementRequest;
import shop.mtcoding.blog.web.course.subject.element.CourseSubjectElementResponse;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectElementService {
    private final SubjectRepository subjectRepository;
    private final SubjectElementRepository subjectElementRepository;

    public CourseSubjectElementResponse.ListDTO 교과목요소목록(Long subjectId) {

        Subject subjectPS = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        List<SubjectElement> subjectElementListPS = subjectElementRepository.findBySubjectId(subjectId);
        return new CourseSubjectElementResponse.ListDTO(subjectPS, subjectElementListPS);
    }

    @Transactional
    public void 교과목요소전체등록(Long subjectId, List<CourseSubjectElementRequest.SaveDTO> reqDTOs) {
        Subject subjectPS = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        List<SubjectElement> subjectElements = reqDTOs.stream().map(saveDTO -> saveDTO.toEntity(subjectPS)).toList();
        subjectElementRepository.saveAll(subjectElements);
    }
}
