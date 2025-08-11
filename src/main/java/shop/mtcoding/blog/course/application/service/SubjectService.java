package shop.mtcoding.blog.course.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.CourseTeacher;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.repository.CourseRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;
import shop.mtcoding.blog.course.application.service.dto.SubjectCommand;
import shop.mtcoding.blog.course.application.service.dto.SubjectOutput;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    public SubjectOutput.MaxList 과정별교과목(Long courseId) {
        List<Subject> subjects = subjectRepository.findAllByCourseId(courseId);
        return new SubjectOutput.MaxList(subjects);
    }

    @Transactional
    public void 교과목등록(Long courseId, SubjectCommand.Save command) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));
        CourseTeacher courseTeacher = CourseTeacher.builder().id(command.courseTeacherId()).build();
        subjectRepository.save(command.toEntity(coursePS, courseTeacher));
    }
}
