package shop.mtcoding.blog.course.subject.element;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.course.subject.Subject;

import java.time.LocalDateTime;

/**
 * 1. 교과목에 대한 세부 내용이다.
 * 2. 교과목이 자바라면 SubjectElement 는 반복문, 조건문, 오버로딩
 */

@NoArgsConstructor
@Getter
@Entity
@Table(name = "subject_element_tb")
public class SubjectElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer no; // 교과목에서의 순번
    private String subtitle;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public SubjectElement(Long id, Integer no, String subtitle, Subject subject, LocalDateTime createdAt) {
        this.id = id;
        this.no = no;
        this.subtitle = subtitle;
        this.subject = subject;
        this.createdAt = createdAt;
    }
}
