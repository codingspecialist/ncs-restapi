package shop.mtcoding.blog.domain.user.teacher;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "teacher_tb")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String sign; // base64 image
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Builder
    public Teacher(Long id, String sign, String name, LocalDateTime createdAt) {
        this.id = id;
        this.sign = sign;
        this.name = name;
        this.createdAt = createdAt;
    }
}
