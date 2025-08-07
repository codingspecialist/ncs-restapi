package shop.mtcoding.blog.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.port.in.SubjectUseCase;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectCommand;
import shop.mtcoding.blog.course.application.port.in.dto.SubjectOutput;
import shop.mtcoding.blog.course.application.port.out.CourseRepositoryPort;
import shop.mtcoding.blog.course.application.port.out.SubjectRepositoryPort;
import shop.mtcoding.blog.course.domain.Course;
import shop.mtcoding.blog.course.domain.CourseTeacher;
import shop.mtcoding.blog.course.domain.Subject;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectService implements SubjectUseCase {
    private final SubjectRepositoryPort subjectRepositoryPort;
    private final CourseRepositoryPort courseRepositoryPort;

    @Override
    public SubjectOutput.MaxList 과정별교과목(Long courseId) {
        List<Subject> subjects = subjectRepositoryPort.findAllByCourseId(courseId);
        return new SubjectOutput.MaxList(subjects);
    }

    @Transactional
    @Override
    public void 교과목등록(Long courseId, SubjectCommand.Save command) {
        Course coursePS = courseRepositoryPort.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));
        CourseTeacher courseTeacher = CourseTeacher.builder().id(command.courseTeacherId()).build();
        subjectRepositoryPort.save(command.toEntity(coursePS, courseTeacher));
    }
}
