package myBlog.core.domainTest;

import myBlog.core.Domain.Board;
import myBlog.core.Domain.Role;
import myBlog.core.Domain.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional(readOnly = true)
public class CreateDomainEntityTest {

    @PersistenceContext EntityManager em;

    @Test
    @Transactional
    public void 유저생성() throws Exception{
        //given
        User user = new User().builder().username("Ahn Kwang Hyun")
                .email("agh0314@gmail.com").password("agh@p970314").build();
        
        //when
        em.persist(user);
        //then
        User findUser = em.find(User.class, user.getUserId());
        System.out.println("findUser.getUserId() = " + findUser.getUserId());
   }

   @Test
   @Transactional
   public void 유저_게시물_생성() throws Exception{
       //given
       User user = User.builder().username("Ahn Kwang Hyun")
               .email("agh0314@gmail.com").password("agh@p970314")
               .role(Role.ADMIN).boards(new ArrayList<>()).build();
       em.persist(user);

       Board board = new Board();
       board.setTitle("dwqddwq");
       em.persist(board);

       board.setUser(user);
       //when
       Board board1 = em.find(Board.class, board.getBoardId());
       User user1 = em.find(User.class, user.getUserId());
       //then

       Assertions.assertThat(user1.getUserId()).isEqualTo(user.getUserId());
       Assertions.assertThat(board1.getBoardId()).isEqualTo(board.getBoardId());

    }
}
