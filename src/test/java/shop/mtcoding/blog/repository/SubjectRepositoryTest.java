package shop.mtcoding.blog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.blog.domain.course.subject.SubjectRepository;

@DataJpaTest
public class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    public void findByCourseId_test() {
        Long courseId = 1L;

        subjectRepository.findByCourseId(courseId);
    }

}
