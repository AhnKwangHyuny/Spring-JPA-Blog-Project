package myBlog.core.controller;

import myBlog.core.Domain.User;
import myBlog.core.dto.ResponseDto;
import myBlog.core.repository.UserRepository;
import myBlog.core.web.LoginForm;
import myBlog.core.web.SessionConstants;
import myBlog.core.web.UserJoinForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encode; // 암호화 객체

    /*
     * << spring security 허용 요청 모음 >>
     * 인증 안된 회원 출입할 수 있는 경로를 /auth/** 허용
     * 그냥 주소가 / 이면 index.html 허용
     * static 이하에 있는 /js/** , /css/**, /image/** 허용
     */


    //


    /*
    * 회원가입
    * */

    // 회원가입 폼 연결
    @GetMapping(value = "/auth/join")
    public String joinForm(Model model) {
        model.addAttribute("form" , new UserJoinForm());

        return "user/joinForm";
    }

    // create new user
//    @PostMapping(value="/auth/join")
//    public String create(@RequestBody @Valid UserJoinForm userJoinForm
//            , BindingResult result , Model model){
//
//        if(result.hasErrors()){
//            List<ObjectError> list =  result.getAllErrors();
//            for(ObjectError e : list) {
//                System.out.println(e.getDefaultMessage());
//            }
//            return "user/joinForm";
//        }
//
//        User user = User.builder().username(userJoinForm.getUsername())
//                .password(userJoinForm.getPassword()).email(userJoinForm.getEmail())
//                .build();
//
//        try{
//            userRepository.save(user);
//        } catch (Exception e){
//            System.out.println("error message = " + e.getMessage() );
////            return "redirect:/user/joinForm";
//            model.addAttribute("errorMessage" , "동일한 Email이 존재합니다.");
//            return "user/joinForm";
//        }
//
//        return "index";
//    }

    /*
     * 로그인
     **/

    @GetMapping(value="/auth/login")
    public String loginForm(Model model, HttpSession session){

//        if(session.getAttribute(SessionConstants.LOGIN_USER) != null){
//            return "redirect:/";
//        }

        model.addAttribute("form" , new LoginForm());

        return "user/loginForm";
    }

    // logout request
    @GetMapping(value="/user/logout")
    public String logout(HttpSession session){
        Object loginSession = session.getAttribute(SessionConstants.LOGIN_USER);

        if(loginSession == null){
            return "redirect:/";
        }

        session.invalidate(); // 세션 삭제

        return "redirect:/";
    }

}
