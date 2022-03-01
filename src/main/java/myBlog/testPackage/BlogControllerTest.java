package myBlog.testPackage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BlogControllerTest {

    @GetMapping(value = "/test/hello")
    public String test(){
        return "<h1>hello spring boot</h1>";
    }

}
