package shop.mtcoding.blog.course;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog.course.student.Student;
import shop.mtcoding.blog.course.student.StudentRepository;
import shop.mtcoding.blog.course.subject.Subject;
import shop.mtcoding.blog.course.subject.SubjectRepository;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CourseService {
    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void 과정등록(CourseRequest.SaveDTO reqDTO) {
        courseRepository.save(reqDTO.toEntity());
    }

    public CourseResponse.PagingDTO 과정목록(Pageable pageable) {
        Page<Course> paging = courseRepository.findAll(pageable);
        return new CourseResponse.PagingDTO(paging);
    }

    public CourseResponse.DetailDTO 과정상세(Long courseId) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> subjectListPS = subjectRepository.findByCourseId(coursePS.getId());
        List<Student> studentListPS = studentRepository.findByCourseId(coursePS.getId());
        return new CourseResponse.DetailDTO(coursePS, subjectListPS, studentListPS);
    }
}
