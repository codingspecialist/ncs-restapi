package shop.mtcoding.blog.course.application.port.in;

import shop.mtcoding.blog.course.application.port.in.dto.SubjectCommand;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectOutput;

public interface SubjectUseCase {
    SubjectOutput.MaxList 과정별교과목(Long courseId);

    void 교과목등록(Long courseId, SubjectCommand.Save command);
}
