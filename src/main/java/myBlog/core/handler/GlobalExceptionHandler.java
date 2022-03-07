package myBlog.core.handler;

import myBlog.core.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice // 모든 exception 발생 시 위 클래스로 매핑해줌
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public ResponseDto<String> handleArgumentException(Exception e){

        System.out.println("GlobalExceptionHandler:error: " + e.getMessage());

        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value() , e.getMessage());
    }
}
