package myBlog.core.dummyControllerTest;

import myBlog.core.web.UpdateUserRequestDTO;
import myBlog.core.Domain.User;
import myBlog.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
public class DummyControllerTest {

    @Autowired // DI
    private UserRepository userRepository;


    @DeleteMapping(value="/dummy/users/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            user.orElseThrow(()-> {
                return new IllegalArgumentException("해당 아이디의 회원이 존재하지 않습니다.");
            });
        } else {
            Long userId = user.get().getUserId();
            userRepository.deleteById(userId);
        }

        return user.get().getUsername() + " 회원이 삭제되었습니다.";
    }

    // http://localhost:8000/blog/dummy/users/{userId}
    // save 함수는 entity id를 전달하지 않으면 insert해주고
    // save 함수는 entity id를 전달하면 해당 id에 대한 데이터가 있으면 update
    // 없으면 insert를 해준다 (merge)
    // save 대신 transactional annotation을 활용하여 dirty checking 하는 방법도 있음
    @PutMapping(value = "/dummy/users/{id}")
    public User updateUser(@PathVariable("id") Long userId ,
                             @RequestBody UpdateUserRequestDTO updateUserRequestDTO ){

        Optional<User> user = userRepository.findById(userId);

        User updatedUser;

        if(user.isEmpty()){
            updatedUser = user.orElseGet(new Supplier<User>() {
                @Override
                public User get() {
                    return new User(updateUserRequestDTO);
                }
            });
        } else {
            updatedUser = user.get().update(updateUserRequestDTO);
            // save : update or create
            // if entity object id exists
            // then update entity
            // if entity object id do not exists
            // then insert entity
            userRepository.save(updatedUser);
        }

        return updatedUser;
    }

    // http://localhost:8000/blog/dummy/users
    @GetMapping(value = "/dummy/users")
    public List<User> list(){
        return userRepository.findAll();
    }

    // paging test
    // paging strategy : 2건씩 아이디를 기준으로 내림차순
    @GetMapping(value = "/dummy/user/page")
    public List<User> pageList(@PageableDefault(size = 2 , sort = "userId" , direction = Sort.Direction.DESC)Pageable pageable){
        List<User> users = userRepository.findAll(pageable).getContent();
        return users;
    }

    // http://localhost:8000/blog/dummy/user/{id}
    @PostMapping(value = "/dummy/join")
    public String join_test(@RequestBody User user){
        System.out.println("username = " + user.getUsername());
        System.out.println("password = " + user.getPassword());
        System.out.println("email = " + user.getEmail() );

        User savedUser = userRepository.save(user);
        System.out.println(savedUser.equals(user));

        return "회원가입이 완료되었습니다.";
    }

    // {id} 주소로 파라미터를 전달 받을 수 있음
    @GetMapping(value = "/dummy/user/{id}")
    public User detail(@PathVariable("id") Long id){

        // Optional로 null인지 판단 , 후처리 method로 orElseGet , orElseThrow 등이 있음
        // 공식 스펙에는 throw IllegalArgumentExeception을 실행되게 할 수 있음
//        User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
//                @Override
//                public IllegalArgumentException get() {
//                    return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
//                }
//            }
//        );

        // lambda 식
        User user = userRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("해당 사용자는 없습니다.");
        });

        // 요청 : 웹브라우저
        // user 객체 = java object
        // 변환( 웹브라우저가 이해할 수 있는 데이터 ex) json)
        // 스프링부트 = messageConvertor라는 것이 응답시에 자동 작동함
        // 만약에 java object를 리턴하게 되면 messageConvertor가 Jackson 라이브러리를 호출하여
        // user 오브젝트를 json으로 변환해서 브라우저에게 던져줌
        return user;
    }
}
