package shop.mtcoding.blog.course.student;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog._core.utils.MyUtil;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.course.CourseRepository;
import shop.mtcoding.blog.user.UserRepository;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public StudentResponse.PagingDTO 모든학생목록(Pageable pageable) {
        Page<Student> paging = studentRepository.findAll(pageable);
        return new StudentResponse.PagingDTO(paging);
    }

    @Transactional
    public void 학생등록(Long courseId, StudentRequest.SaveDTO reqDTO) {
        // 1. 과정 존재 확인
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        // 2. 인증 코드 발급 (유저 회원가입시 매칭하기 위해)
        String authCode = MyUtil.generateAuthCode();

        // 3. 학생 저장
        Student student = reqDTO.toEntity(coursePS, authCode);
        studentRepository.save(student);
    }
}
