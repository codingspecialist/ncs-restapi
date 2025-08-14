package shop.mtcoding.blog.exam.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.exam.application.domain.QuestionOption;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
}
