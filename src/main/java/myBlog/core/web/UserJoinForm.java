package myBlog.core.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinForm {

    @NotEmpty(message = "이름은 필수 입력사항 입니다.")
    private String username;

    @NotEmpty(message = "회원 비밀번호는 필수 입니다.")
    private String password;

    @NotEmpty(message = "회원 이메일은 필수 입니다.")
    @Email
    private String email;

}
