package myBlog.core.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BoardNotFoundException extends UsernameNotFoundException {
    public BoardNotFoundException(String msg) {
        super(msg);
    }

    public BoardNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
