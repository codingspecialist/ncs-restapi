package shop.mtcoding.blog.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.port.in.SubjectElementUseCase;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectElementCommand;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectElementOutput;
import shop.mtcoding.blog.course.application.port.out.SubjectElementRepositoryPort;
import shop.mtcoding.blog.course.application.port.out.SubjectRepositoryPort;
import shop.mtcoding.blog.course.domain.Subject;
import shop.mtcoding.blog.course.domain.SubjectElement;

import java.util.HashSet;
import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectElementService implements SubjectElementUseCase {
    private final SubjectRepositoryPort subjectRepositoryPort;
    private final SubjectElementRepositoryPort subjectElementRepositoryPort;

    public SubjectElementOutput.MaxList 교과목요소목록(Long subjectId) {

        Subject subjectPS = subjectRepositoryPort.findByIdWithElements(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        return new SubjectElementOutput.MaxList(subjectPS);
    }

    @Transactional
    public void 교과목요소전체등록(Long subjectId, List<SubjectElementCommand.Save> command) {
        // 1. 교과목 존재확인
        Subject subjectPS = subjectRepositoryPort.findById(subjectId)
                .orElseThrow(() -> new Exception404("해당 교과목을 찾을 수 없습니다"));

        // 2. 중복 순번 조회 (한 번의 쿼리로!)
        List<Integer> requestedNos = command.stream()
                .map(save -> save.no())
                .toList();

        List<Integer> existingNos = subjectElementRepositoryPort.findNosBySubjectIdAndNoIn(subjectId, requestedNos);

        if (!existingNos.isEmpty()) {
            throw new Exception400("이미 존재하는 순번: " + new HashSet<>(existingNos));
        }

        // 3. 교과목 요소 저장
        List<SubjectElement> subjectElements = command.stream().map(saveDTO -> saveDTO.toEntity(subjectPS)).toList();
        subjectElementRepositoryPort.saveAll(subjectElements);
    }
}
