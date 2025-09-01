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
import shop.mtcoding.blog.course.web.dto.SubjectRequest;
import shop.mtcoding.blog.course.web.dto.SubjectResponse;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    public SubjectResponse.MaxList 과정별교과목(Long courseId) {
        List<Subject> subjects = subjectRepository.findAllByCourseId(courseId);
        return new SubjectResponse.MaxList(subjects);
    }

    @Transactional
    public void 교과목등록(Long courseId, SubjectRequest.Save request) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));
        CourseTeacher courseTeacher = CourseTeacher.builder().id(request.courseTeacherId()).build();

        Subject subject = Subject.builder()
                .code(request.code())
                .title(request.title())
                .purpose(request.purpose())
                .ncsType(request.ncsType())
                .gradeLevel(request.gradeLevel())
                .totalTime(request.totalTime())
                .scorePolicy(request.scorePolicy())
                .learningWay(request.learningWay())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .courseTeacher(courseTeacher)
                .course(coursePS)
                .build();

        subjectRepository.save(subject);
    }
}
