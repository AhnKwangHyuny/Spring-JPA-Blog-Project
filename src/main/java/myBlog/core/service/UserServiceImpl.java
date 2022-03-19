package myBlog.core.service;

import myBlog.core.Domain.User;
import myBlog.core.repository.UserRepository;
import myBlog.core.web.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌, 자동 IOC
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Transactional
    public void join(User user){
        String rawPassword = user.getPassword();
        String encPassword = encoder.encode(rawPassword); // 해쉬화

        user.setPassword(encPassword);

        System.out.println("encPassword = " + encPassword);
        
        userRepository.save(user);
    }

//    @Transactional(readOnly = true) // select 할때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료 (정합성 보장)
//    public User login(LoginForm loginform){
//        return userRepository.findByEmailAndPassword(loginform.getEmail(), loginform.getPassword());
//    }


}

