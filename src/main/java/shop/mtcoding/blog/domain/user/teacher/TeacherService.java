package shop.mtcoding.blog.domain.user.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog.web.user.teacher.TeacherResponse;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public List<TeacherResponse.DTO> 강사목록() {
        return teacherRepository.findAllWithUser().stream().map(TeacherResponse.DTO::new).toList();
    }
}
