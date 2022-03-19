package myBlog.core.config.auth;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import myBlog.core.Domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 userdetails 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다.
@NoArgsConstructor
public class PrincipalDetail implements UserDetails {
    private User user; // 콤포지션 : 객체를 품고 있는것

    public PrincipalDetail(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();

//        collectors.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//                return "ROLE_" + user.getRole(); // spring role 규칙
//            }
//        });

        collectors.add(()-> { return "ROLE_" + user.getRole();});

        return collectors;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Long getUserId(){
        return user.getUserId();
    }

    public String getEmail(){
        return user.getEmail();
    }

    // 계정이 만료되지 않았는지 리턴한다.(true : 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있는지 리턴 (true : 안잠김)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호가 만료되었는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화(사용가능)인지 리턴
    @Override
    public boolean isEnabled() {
        return true;
    }
}
