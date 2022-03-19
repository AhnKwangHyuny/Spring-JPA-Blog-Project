package myBlog.core.service;

import myBlog.core.Domain.Board;
import myBlog.core.dto.BoardUpdateDto;
import myBlog.core.dto.NewBoardDto;
import myBlog.core.dto.ResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.Lob;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface BoardService {
    Long save(NewBoardDto newBoardDto);

    Page<Board> findAll(Pageable pageable);

    Board findById(Long boardId);

    void updateById (BoardUpdateDto boardUpdateDto);

    void viewCountUp(Long boardId);
}

