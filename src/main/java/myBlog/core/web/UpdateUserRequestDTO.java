package myBlog.core.web;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {

    private String username;
    private String password;
    private String email;
}
