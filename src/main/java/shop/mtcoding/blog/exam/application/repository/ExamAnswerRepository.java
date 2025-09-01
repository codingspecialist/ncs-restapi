package shop.mtcoding.blog.exam.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.exam.application.domain.ExamAnswer;

public interface ExamAnswerRepository extends JpaRepository<ExamAnswer, Long> {

}
