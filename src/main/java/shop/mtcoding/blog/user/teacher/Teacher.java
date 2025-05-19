package shop.mtcoding.blog.user.teacher;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.user.User;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "teacher_tb")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Lob
    private String sign; // 선생님이라면 서명 (base64 저장)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Teacher(Long id, User user, String sign, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.sign = sign;
        this.createdAt = createdAt;
    }
}
