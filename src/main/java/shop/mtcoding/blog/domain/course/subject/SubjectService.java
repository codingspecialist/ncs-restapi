package shop.mtcoding.blog.domain.course.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.Exception400;
import shop.mtcoding.blog.core.errors.exception.Exception404;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.CourseRepository;
import shop.mtcoding.blog.web.course.subject.CourseSubjectRequest;
import shop.mtcoding.blog.web.course.subject.CourseSubjectResponse;
import shop.mtcoding.blog.web.exam.TeacherExamResponse;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    // Query 수정 완료
    public List<TeacherExamResponse.SubjectDTO> 과정별교과목(Long courseId) {
        Course coursePS = courseRepository.findByIdWithSubject(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        return coursePS.getSubjects().stream().map(TeacherExamResponse.SubjectDTO::new).toList();
    }

    public CourseSubjectResponse.PagingDTO 모든교과목목록(Pageable pageable) {
        Page<Subject> paging = subjectRepository.findAll(pageable);
        return new CourseSubjectResponse.PagingDTO(paging);
    }

    public List<CourseSubjectResponse.DTO> 모든교과목목록() {
        List<Subject> subjectList = subjectRepository.findAll();
        return subjectList.stream().map(CourseSubjectResponse.DTO::new).toList();
    }

    @Transactional
    public void 교과목등록(Long courseId, CourseSubjectRequest.SaveDTO reqDTO) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        List<Subject> subjectListPS = subjectRepository.findByCourseId(coursePS.getId());

        Boolean isSameNo = subjectListPS.stream().anyMatch(subject -> subject.getNo().equals(reqDTO.getNo()));


        if (isSameNo) {
            String subjectNos = subjectListPS.stream()
                    .map(subject -> subject.getNo().toString())
                    .collect(Collectors.joining(","));
            throw new Exception400("동일한 교과목 번호는 등록할 수 없습니다. \\n현재 교과목 번호들 : [" + subjectNos + "]");
        }
        subjectRepository.save(reqDTO.toEntity(coursePS));
    }
}
