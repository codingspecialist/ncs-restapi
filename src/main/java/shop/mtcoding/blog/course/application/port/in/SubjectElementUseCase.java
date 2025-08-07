package shop.mtcoding.blog.course.application.port.in;

import shop.mtcoding.blog.course.application.port.in.dto.SubjectElementCommand;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectElementOutput;

import java.util.List;

public interface SubjectElementUseCase {
    SubjectElementOutput.MaxList 교과목요소목록(Long subjectId);

    void 교과목요소전체등록(Long subjectId, List<SubjectElementCommand.Save> commands);
}
