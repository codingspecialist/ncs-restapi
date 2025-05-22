package shop.mtcoding.blog.user.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
