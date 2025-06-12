package shop.mtcoding.blog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.mtcoding.blog.domain.course.exam.ExamModel;
import shop.mtcoding.blog.domain.course.exam.ExamQueryRepository;

import java.util.List;

@Import(ExamQueryRepository.class)
@DataJpaTest
public class ExamRepositoryTest {

    @Autowired
    private ExamQueryRepository examRepository;

    @Test
    public void findValidExamsBySubjectId_test() {
        // given

        // when
        List<ExamModel.Result> exams = examRepository.findExamResult(1L);
        System.out.println("exams " + exams.size());
        exams.forEach(exam -> {
            System.out.println(exam);
        });
        // then
    }
}
