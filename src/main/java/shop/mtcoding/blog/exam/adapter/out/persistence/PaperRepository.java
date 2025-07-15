package shop.mtcoding.blog.exam.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.exam.application.port.out.PaperRepositoryPort;
import shop.mtcoding.blog.exam.domain.Exam;

public interface PaperRepository extends JpaRepository<Exam, Long>, PaperRepositoryPort {
}
