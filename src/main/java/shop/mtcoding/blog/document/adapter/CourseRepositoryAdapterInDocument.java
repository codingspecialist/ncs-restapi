package shop.mtcoding.blog.document.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import shop.mtcoding.blog.course.application.domain.Course;
import shop.mtcoding.blog.course.application.domain.Subject;
import shop.mtcoding.blog.course.application.domain.SubjectElement;
import shop.mtcoding.blog.course.application.repository.CourseRepository;
import shop.mtcoding.blog.course.application.repository.SubjectRepository;
import shop.mtcoding.blog.course.application.repository.SubjectElementRepository;

import java.util.List;
import java.util.Optional;

@Component("courseRepositoryAdapterInDocument")
@RequiredArgsConstructor
public class CourseRepositoryAdapterInDocument {

    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final SubjectElementRepository subjectElementRepository;

    public Page<Course> findCoursesByTeacherId(Long teacherId, Pageable pageable) {
        return courseRepository.findAllByTeacherId(teacherId, pageable);
    }

    public Optional<Subject> findSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId);
    }

    public List<Subject> findSubjectsByCourseId(Long courseId) {
        return subjectRepository.findAllByCourseId(courseId);
    }

    public List<SubjectElement> findSubjectElementsBySubjectId(Long subjectId) {
        return subjectElementRepository.findAllBySubjectId(subjectId);
    }
}
