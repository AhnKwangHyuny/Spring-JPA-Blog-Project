package myBlog.core.config.auth;

import myBlog.core.Domain.User;
import myBlog.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrincipalDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 스프링이 로그인 요청을 가로챌 때, username , password 변수 2개를 가로채는데
    // password 부분 처리는 알아서 함,
    // username이 db에 있는지만 확인해주면 됨
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(()->{
            throw new UsernameNotFoundException("userEmail : " + email + " not Found!");
        });

        return new PrincipalDetail(user.get()); // 시큐리티의 센션에 유저 정보가 저장됨
    }
}
