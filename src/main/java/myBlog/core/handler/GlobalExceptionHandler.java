package myBlog.core.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice // 모든 exception 발생 시 위 클래스로 매핑해줌
@RestController
public class GlobalExceptionHandler {

    // http://localhost:8000/blog/dummy/exception
    @ExceptionHandler(value = Exception.class)
    public String ExceptionHandler(Exception e){
        return "<h1>" + e.getMessage() + "</h1>";
    }
}
