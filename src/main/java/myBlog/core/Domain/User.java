package myBlog.core.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import myBlog.core.web.UpdateUserRequestDTO;
import myBlog.core.web.UserJoinForm;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@DynamicInsert // insert 시 null인 field 제외
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity // User object -> mysql table로 생성됨
public class User extends BaseEntity {

    public User(UpdateUserRequestDTO updateUserRequestDTO) {
        this.setUsername(updateUserRequestDTO.getUsername());
        this.setEmail(updateUserRequestDTO.getEmail());
        this.setPassword(updateUserRequestDTO.getPassword());

    }

    @Column(name = "user_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue : numbering 전략 -> 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private Long userId; // oracle : sequence , mySql : auto_increment

    @Column(nullable = false, length = 30)
    private String username; // 아이디

    @Column(nullable = false, length = 100) // 비밀번호를 해쉬로 암호화하여 저장하기 위해 길이 충분히 줌
    private String password; // 패스워드

    @Column(name = "user_role", columnDefinition = "varchar(30) default 'USER' ")
    @Enumerated(EnumType.STRING)
    private Role role; // Enum을 쓰자!

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    // mappedBy 연관관계의 주인이 아니다 (난 fk가 아니에요) db에 칼럼을 만들지 말라는 의미
    private List<Reply> replies = new ArrayList<>();

    //== entity business ==//
    public User update(UpdateUserRequestDTO updateUserRequestDTO) {
        this.setUsername(updateUserRequestDTO.getUsername());
        this.setEmail(updateUserRequestDTO.getEmail());
        this.setPassword(updateUserRequestDTO.getPassword());

        return this;
    }

    public static User create(UserJoinForm userJoinForm) {
        User user = new User();
        user.setUsername(userJoinForm.getUsername());
        user.setEmail(userJoinForm.getEmail());
        user.setPassword(userJoinForm.getPassword());

        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", email='" + email + '\'' +
                '}';
    }
}