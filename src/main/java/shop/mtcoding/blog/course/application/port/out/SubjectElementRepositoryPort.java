package shop.mtcoding.blog.course.application.port.out;

import org.springframework.data.repository.query.Param;
import shop.mtcoding.blog.course.domain.SubjectElement;

import java.util.List;

public interface SubjectElementRepositoryPort {
    void saveAll(List<SubjectElement> elements);

    List<SubjectElement> findAllBySubjectId(@Param("subjectId") Long subjectId);

    List<Integer> findNosBySubjectIdAndNoIn(@Param("subjectId") Long subjectId, @Param("nos") List<Integer> nos);
}
