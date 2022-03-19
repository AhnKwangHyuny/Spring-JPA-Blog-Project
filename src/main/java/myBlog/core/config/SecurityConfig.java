package myBlog.core.config;

import myBlog.core.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 빈 등록 : 스프링 컨테이너에서 객체를 관리
@Configuration // ioc 관리
@EnableWebSecurity // 시큐리티 필터 등록 설정 가능케 함
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    // hash값 변경 암호화
    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder(); // BCrptPasswordEncoder 객체 ioc화(스프링이 관리)
    }

    // 시큐리티가 대신 로그인해주는데 password를 가로채기를 하는데
    // 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
    // 같은 해쉬로 암호화해서 db에 있는 해쉬랑 비교할 수 있음

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable() // csrf 토큰 비활성화 , spring security는 csrf토큰이 없는 요청은 자동으로 막아버림(ex ajax 통신)
        .authorizeRequests()
            .antMatchers("/auth/**" , "/js/**" , "/css/**" , "/image/**" , "/dummy/**") // auth path는 누구나 허용
            .permitAll()
            .anyRequest() // /auth 경로가 아닌 모든 주소 인증 필요
            .authenticated()
        .and()
            .formLogin()
            .usernameParameter("email")
            .passwordParameter("password")
            .loginPage("/auth/login")
            .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신 로그인
            .defaultSuccessUrl("/"); //정상적을 요청이 완료되면 main page 이동
    }

}
