package myBlog.core.web;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty(message = "email이 누락되었습니다.")
    private String email;

    @NotEmpty(message = "password가 누락되었습니다.")
    private String password;
}
