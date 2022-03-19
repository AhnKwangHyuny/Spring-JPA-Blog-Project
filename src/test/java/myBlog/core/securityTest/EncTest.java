package myBlog.core.securityTest;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncTest {
    @Test 
    public void 비밀번호_암호화() throws Exception{
        //given 
        String encPassword = new BCryptPasswordEncoder().encode("1234");
        //when 
        System.out.println("encPassword = " + encPassword);
        //then 
       }
}
