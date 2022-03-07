package myBlog.core.repository;

import myBlog.core.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// DAO
// 자동으로 bean 동록이 된다.
// @Repository 생략 가능
public interface UserRepository extends JpaRepository<User, Long> {

}

//jpa naming strategy
// findByUsernameAndPassword -> select * from User where username = ? and password = ?
//User findByEmailAndPassword(String email , String password);

// native query 사용가능
//    @Query(value="select * from User where username = ? and password = ?" , nativeQuery = true)
//    User login(String email , String password);
