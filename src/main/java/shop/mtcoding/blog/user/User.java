package shop.mtcoding.blog.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.user.student.Student;
import shop.mtcoding.blog.user.teacher.Teacher;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "user_tb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String email;
    private String name; // 이름
    @Enumerated(EnumType.STRING)
    private UserEnum role; // 학생, 강사, 직원, 팀장, 원장
    @CreationTimestamp // pc -> db (날짜주입)
    private LocalDateTime createdAt;

    // 학생 인증
    public void studentAuthentication(String username, String password, String email, UserEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /// //////////////////////////////////////////// 단순 조회용도!!!!!!!!!!!!!!!!!!!
    // 이건 캐스캐이드 저장 불가능함 (FK가 이쪽에 없고, 반대방향에 있음)
    // @Column(unique = true) // OneToOne은 UK가 기본적용됨.
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Student student; // role이 student이면 연결된 객체 필요!! 선생이 먼저 학생을 등록해야 가입가능

    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Teacher teacher;

    // 영속화는 불가능 (객체에 연결만)
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    // 영속화는 불가능 (객체에 연결만)
    public void setStudent(Student student) {
        this.student = student;
    }

    /// //////////////////////////////////////////////////////////////////////////

    @Builder
    public User(Long id, String username, String password, String email, String name, UserEnum role, LocalDateTime createdAt, Student student, Teacher teacher) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
        this.student = student;
        this.teacher = teacher;
    }
}
