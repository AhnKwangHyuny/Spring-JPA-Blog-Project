package myBlog.core.controller;

import myBlog.core.Domain.User;
import myBlog.core.repository.UserRepository;
import myBlog.core.web.LoginForm;
import myBlog.core.web.UserJoinForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /*
    * 회원가입
    * */

    // 회원가입 폼 연결
    @GetMapping(value = "/user/joinForm")
    public String joinForm(Model model) {
        model.addAttribute("form" , new UserJoinForm());

        return "user/joinForm";
    }

    // create new user
    @PostMapping(value="/user/joinForm")
    public String create(@ModelAttribute("form") @Valid UserJoinForm userJoinForm
            , BindingResult result , Model model){

        if(result.hasErrors()){
            List<ObjectError> list =  result.getAllErrors();
            for(ObjectError e : list) {
                System.out.println(e.getDefaultMessage());
            }
            return "user/joinForm";
        }

        User user = User.builder().username(userJoinForm.getUsername())
                .password(userJoinForm.getPassword()).email(userJoinForm.getEmail())
                .build();

        try{
            userRepository.save(user);
        } catch (Exception e){
            System.out.println("error message = " + e.getMessage() );
//            return "redirect:/user/joinForm";
            model.addAttribute("errorMessage" , "동일한 Email이 존재합니다.");
            return "user/joinForm";
        }

        return "index";
    }

    /*
     * 로그인
     **/

    @GetMapping(value="/user/login")
    public String loginForm(Model model){
        model.addAttribute("form" , new LoginForm());

        return "user/loginForm";
    }


}
