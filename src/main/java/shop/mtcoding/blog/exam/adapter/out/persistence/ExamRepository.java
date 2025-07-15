package shop.mtcoding.blog.exam.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.exam.application.port.out.ExamRepositoryPort;
import shop.mtcoding.blog.exam.domain.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long>, ExamRepositoryPort {
}
