package shop.mtcoding.blog.domain.user.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.core.errors.exception.api.Exception404;
import shop.mtcoding.blog.core.utils.MyUtil;
import shop.mtcoding.blog.domain.course.Course;
import shop.mtcoding.blog.domain.course.CourseRepository;
import shop.mtcoding.blog.web.course.student.CourseStudentRequest;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void 학생등록(Long courseId, CourseStudentRequest.SaveDTO reqDTO) {
        // 1. 과정 존재 확인
        Course coursePS = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception404("과정을 찾을 수 없습니다"));

        // 2. 인증 코드 발급 (유니크해야 한다)
        for (int i = 0; i < 5; i++) {
            try {
                String authCode = MyUtil.generateAuthCode();
                Student student = reqDTO.toEntity(coursePS, authCode);
                studentRepository.save(student); // 동시성, 중복시 예외발생
                break;
            } catch (Exception e) {
                log.info("인증코드 중복 발생 : " + e.getMessage());
                // 인증코드 중복 발생 → 재시도
            }
        }
    }
}
