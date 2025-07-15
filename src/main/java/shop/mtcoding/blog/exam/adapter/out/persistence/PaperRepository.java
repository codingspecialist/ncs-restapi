package shop.mtcoding.blog.exam.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.exam.application.port.out.PaperRepositoryPort;

public interface PaperRepository extends JpaRepository<Paper, Long>, PaperRepositoryPort {
}
