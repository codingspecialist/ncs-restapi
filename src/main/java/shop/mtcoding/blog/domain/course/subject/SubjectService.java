package shop.mtcoding.blog.domain.course.subject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.api.Exception400;
import shop.mtcoding.blog._core.errors.exception.api.Exception404;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.CourseRepository;
import shop.mtcoding.blog.domain.user.teacher.Teacher;
import shop.mtcoding.blog.domain.user.teacher.TeacherRepository;
import shop.mtcoding.blog.web.course.subject.CourseSubjectRequest;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public SubjectModel.Items 과정별교과목(Long courseId) {
        List<Subject> subjects = subjectRepository.findAllByCourseId(courseId);
        return new SubjectModel.Items(subjects);
    }

    @Transactional
    public void 교과목등록(Long courseId, CourseSubjectRequest.SaveDTO reqDTO) {
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        Teacher teacher = teacherRepository.findByName(reqDTO.getTeacherName())
                .orElseThrow(() -> new Exception404("선생님을 못찾겠어요"));

        List<Subject> subjectListPS = subjectRepository.findAllByCourseId(coursePS.getId());

        Boolean isSameNo = subjectListPS.stream().anyMatch(subject -> subject.getNo().equals(reqDTO.getNo()));


        if (isSameNo) {
            String subjectNos = subjectListPS.stream()
                    .map(subject -> subject.getNo().toString())
                    .collect(Collectors.joining(","));
            throw new Exception400("동일한 교과목 번호는 등록할 수 없습니다. \\n현재 교과목 번호들 : [" + subjectNos + "]");
        }
        subjectRepository.save(reqDTO.toEntity(coursePS, teacher));
    }
}
