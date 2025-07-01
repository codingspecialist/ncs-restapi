package shop.mtcoding.blog.domainv2222222.user.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherModel.Items 강사목록() {
        return new TeacherModel.Items(teacherRepository.findAllWithUser());
    }

}
