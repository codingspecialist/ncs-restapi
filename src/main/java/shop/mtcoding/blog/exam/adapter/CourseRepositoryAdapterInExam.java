package shop.mtcoding.blog.exam.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.course.application.domain.CourseStudent;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.course.application.repository.CourseStudentRepository;
import shop.mtcoding.blog.course.application.repository.SubjectElementRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Component("courseRepositoryAdapterInExam")
@RequiredArgsConstructor
public class CourseRepositoryAdapterInExam {

    private final SubjectRepository subjectRepository;
    private final SubjectElementRepository subjectElementRepository;
    private final CourseStudentRepository courseStudentRepository;

    public Optional<Subject> findSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId);
    }

    public Optional<SubjectElement> findSubjectElementById(Long elementId) {
        return subjectElementRepository.findById(elementId);
    }

    public Optional<CourseStudent> findCourseStudentByStudentId(Long studentId) {
        return courseStudentRepository.findByStudentId(studentId);
    }

    public List<CourseStudent> findAllByCourseId(Long courseId) {
        return courseStudentRepository.findAllByCourseId(courseId);
    }

    public List<SubjectElement> findAllBySubjectId(Long subjectId) {
        return subjectElementRepository.findAllBySubjectId(subjectId);
    }
}
