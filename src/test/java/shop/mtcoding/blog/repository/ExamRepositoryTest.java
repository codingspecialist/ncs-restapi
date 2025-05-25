package shop.mtcoding.blog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.blog.domain.course.exam.ExamRepository;

@DataJpaTest
public class ExamRepositoryTest {

    @Autowired
    private ExamRepository examRepository;

    @Test
    public void findByOrigin_test() {
        // given

        // when
        //examRepository.findByOrigin(1L,2L);

        // then
    }
}
