package myBlog.core.Domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Board extends BaseEntity{

    @Column(name = "board_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue : numbering 전략 -> 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private Long boardId; // oracle : sequence , mySql : auto_increment

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board" ,fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Reply> replies = new ArrayList<>();

    @Column(nullable = false , length = 255)
    private String title;

    @Lob // 대용량 데이터
    private String content; // 섬머노트 라이브러리 <html>태그가 섞여서 디자인

    @ColumnDefault("0")
    private int count; // 조회수



    //== 연관관계 메소드 ==//
    public void setUser(User user){
        this.user = user;
        user.getBoards().add(this);
    }
}
