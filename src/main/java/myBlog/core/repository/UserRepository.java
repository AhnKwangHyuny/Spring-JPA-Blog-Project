package myBlog.core.repository;

import myBlog.core.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

// DAO
// 자동으로 bean 동록이 된다.
// @Repository 생략 가능
public interface UserRepository extends JpaRepository<User, Long> {

}
