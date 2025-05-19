package shop.mtcoding.blog.course.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.course.CourseRepository;
import shop.mtcoding.blog.course.exam.ExamResponse;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    // TODO : SubjectDTO 수정
    public List<ExamResponse.SubjectDTO> 과정별교과목(Long courseId) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> subjectListPS = subjectRepository.findByCourseId(coursePS.getId());
        return subjectListPS.stream().map(ExamResponse.SubjectDTO::new).toList();
    }

    public SubjectResponse.PagingDTO 모든교과목목록(Pageable pageable) {
        Page<Subject> paging = subjectRepository.findAll(pageable);
        return new SubjectResponse.PagingDTO(paging);
    }

    public List<SubjectResponse.DTO> 모든교과목목록() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList.stream().map(SubjectResponse.DTO::new).toList();
    }

    @Transactional
    public void 교과목등록(Long courseId, SubjectRequest.SaveDTO reqDTO) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        subjectRepository.save(reqDTO.toEntity(coursePS));
    }
}
