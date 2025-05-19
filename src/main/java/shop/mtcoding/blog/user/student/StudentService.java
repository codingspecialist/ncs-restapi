package shop.mtcoding.blog.user.student;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.errors.exception.Exception404;
import shop.mtcoding.blog.course.Course;
import shop.mtcoding.blog.course.CourseRepository;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserEnum;
import shop.mtcoding.blog.user.UserRepository;

import java.util.List;

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

        // 2. 유저 저장 (학생 회원가입시에 업데이트 해야함)
        User user = User.builder()
                .name(reqDTO.getName())
                .role(UserEnum.STUDENT)
                .build();
        User userPS = userRepository.save(user);

        // 3. 학생 저장
        Student student = reqDTO.toEntity(coursePS, userPS);
        studentRepository.save(student);

        // 4. 학생 이름으로 조회하여 과정별 학생 번호 이름순으로 부여
        List<Student> studentListPS = studentRepository.findByCourseId(courseId);
        for (int i = 0; i < studentListPS.size(); i++) {
            Student st = studentListPS.get(i);
            st.updateStudentNo(i + 1);
        }
    }
}
