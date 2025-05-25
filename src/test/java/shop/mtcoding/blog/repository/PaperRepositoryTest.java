package shop.mtcoding.blog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.mtcoding.blog.domain.course.subject.paper.PaperRepository;

@DataJpaTest
public class PaperRepositoryTest {

    @Autowired
    private PaperRepository paperRepository;

    @Test
    public void findByCourseId_test() {
        // given
        Long courseId = 1L;

        // when
        paperRepository.findByCourseId(courseId);

        // then
    }
}
