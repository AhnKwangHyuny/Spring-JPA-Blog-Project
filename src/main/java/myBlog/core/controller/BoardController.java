package myBlog.core.controller;

import lombok.RequiredArgsConstructor;
import myBlog.core.Domain.Board;
import myBlog.core.config.auth.PrincipalDetail;
import myBlog.core.service.BoardService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value="/boards/save")
    public String save(HttpSession session , Model model){
        // 현재 인증된 유저 엔티티 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();

        if (principal.getUserId() == null) {
            throw new IllegalArgumentException("BoardController : save -> userId do not exists ");
        } else {
            model.addAttribute("userId" , principal.getUserId());
        }

        return "board/saveForm";
    }

    // 게시글 상세보기
    @GetMapping(value="/boards/{id}")
    public String detail(@PathVariable("id") Long boardId , Model model,
                         HttpServletRequest request , HttpServletResponse response){

        // 로그인 한 유저 principal 객체 받기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();

        // 조회수 중복 방지
        viewCountUp(boardId, request , response);

        Board board = boardService.findById(boardId);

        model.addAttribute("board" , board);
        model.addAttribute("principal" , principal);

        return "board/detail";
    }

    /*
    * 게시글 업데이트
    * */

    @GetMapping(value="/boards/{id}/update")
    public String update(Model model , @PathVariable("id") Long boardId ){

        // 현재 인증된 유저 엔티티 가져오기
        PrincipalDetail principal = (PrincipalDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getUserId() == null) {
            throw new IllegalArgumentException("BoardController : update -> userId do not exists ");
        } else {
            Board board = boardService.findById(boardId);

            if(principal.getUserId() != board.getUser().getUserId()){
                throw new IllegalArgumentException("BoardController : update => 게시글 작성자와 현재 사용자의 아이디가 일치하지 않습니다! (수정 권한 없음!) ");
            }

            model.addAttribute("board" , board);
        }

        return "board/updateForm";
    }



    /**
     조회수 중복 방지
     **/
    private void viewCountUp(Long id, HttpServletRequest request, HttpServletResponse response) {

        Cookie oldCookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("postView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + id.toString() + "]")) {
                boardService.viewCountUp(id);
                oldCookie.setValue(oldCookie.getValue() + "_[" + id + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                response.addCookie(oldCookie);
            }
        } else {
            boardService.viewCountUp(id);
            Cookie newCookie = new Cookie("postView","[" + id + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            response.addCookie(newCookie);
        }
    }

}
