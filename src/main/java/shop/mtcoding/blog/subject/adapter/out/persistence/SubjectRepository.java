package shop.mtcoding.blog.subject.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.mtcoding.blog.exam.domain.Exam;
import shop.mtcoding.blog.subject.application.port.out.SubjectRepositoryPort;

public interface SubjectRepository extends JpaRepository<Exam, Long>, SubjectRepositoryPort {
}
