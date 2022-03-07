package myBlog.core.controller.api;

import myBlog.core.Domain.User;
import myBlog.core.dto.ResponseDto;
import myBlog.core.service.UserService;
import myBlog.core.web.LoginForm;
import myBlog.core.web.SessionConstants;
import myBlog.core.web.UserJoinForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class UserApiController {
    /*
     * << spring security 허용 요청 모음 >>
     * 인증 안된 회원 출입할 수 있는 경로를 /auth/** 허용
     * 그냥 주소가 / 이면 index.html 허용
     * static 이하에 있는 /js/** , /css/**, /image/** 허용
     */

    @Autowired
    private UserService userService;

    @PostMapping("/auth/join")
    public ResponseDto<Integer> save(@RequestBody @Valid UserJoinForm userJoinForm) {
        System.out.println("UserApiController: save 호출됨");
        userService.join(User.create(userJoinForm));

        // 성공 시 , 실패하면 globalException class에서 에러 핸들링
        return new ResponseDto<Integer>(HttpStatus.OK.value() , 1);
    }

    /*
    *  로그인 api
    * */

//    @PostMapping(value="/api/user/login")
//    public ResponseDto<Integer> login(@RequestBody @Valid LoginForm loginform , HttpSession session) {
//        // find service method
//        User principal = userService.login(loginform);
//
//        if(principal != null){
//            // session 생성
//            session.setAttribute(SessionConstants.LOGIN_USER , principal);
//        } else {
//            return new ResponseDto<Integer>(HttpStatus.BAD_REQUEST.value(), -1);
//        }
//
//        return new ResponseDto<Integer>(HttpStatus.OK.value() , 1);
//    }
}
