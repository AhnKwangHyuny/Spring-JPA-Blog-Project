package myBlog.core.controller.api;

import myBlog.core.Domain.Board;
import myBlog.core.dto.BoardUpdateDto;
import myBlog.core.dto.NewBoardDto;
import myBlog.core.dto.ResponseDto;
import myBlog.core.repository.BoardRepository;
import myBlog.core.repository.UserRepository;
import myBlog.core.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
public class boardApiController {

    @Autowired
    private BoardService boardService;

    @PostMapping(value = "/boards/save")
    public Long save(@RequestBody NewBoardDto newBoardDto){
        Long boardId = boardService.save(newBoardDto);

        System.out.println("entity id : " + boardId + " entity name : board, 성공적으로 저장했습니다. = ");

        return boardId;
    }

    //@AuthenticationPrincipal PrincipalDetail principal

    /*
    * 게시글 업데이트 api
    * */
    @PutMapping(value="/boards/{id}/update")
    public ResponseDto<Board> update(@RequestBody BoardUpdateDto boardUpdateDto
            , @PathVariable("id") Long boardId){
        // 게시글 업데이트
        boardService.updateById(boardUpdateDto);

        // 정상 수행 후 dirty checking 된 board entity 가져오기 (1차 캐시)
        Board board = boardService.findById(boardId);

        return new ResponseDto<Board>(HttpStatus.OK.value(), board);
    }



/*
* paging api
* */

//    @GetMapping(value = "/page")
//    public Page<Board> index(Model model,
//                             @PageableDefault(size = 5 ,sort = "boardId" , direction = Sort.Direction.DESC) Pageable pageable){
//        // paging 된 board entities 받아오기
//        Page<Board> boards = boardService.findAll(pageable);
//
//        // model 에 boards 담아서 템플릿에 넘겨줌
//        return boards;
//    }

}
