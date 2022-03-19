package myBlog.core.controller;

import lombok.RequiredArgsConstructor;
import myBlog.core.Domain.Board;
import myBlog.core.config.auth.PrincipalDetail;
import myBlog.core.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private final BoardService boardService;

    //@AuthenticationPrincipal PrincipalDetail principal
    @GetMapping(value = {"/" , ""})
    public String index(Model model,
                        @PageableDefault(size = 2 ,sort = "boardId" , direction = Sort.Direction.DESC) Pageable pageable,
                        HttpServletRequest request){

        // paging 된 board entities 받아오기
        Page<Board> boards = boardService.findAll(pageable);

        int startPage = Math.max(0 , boards.getPageable().getPageNumber() - 5);
        int endPage = Math.min(boards.getTotalPages() , boards.getPageable().getPageNumber() + 5);

        System.out.println("endPage = " + endPage + "startPage = " + startPage) ;

        // model 에 boards 담아서 템플릿에 넘겨줌
        model.addAttribute("startPage" , startPage);
        model.addAttribute("endPage" , endPage);
        model.addAttribute("boards" , boards);

        System.out.println("pageSize = " + boards);
        return "index";
    }

}
