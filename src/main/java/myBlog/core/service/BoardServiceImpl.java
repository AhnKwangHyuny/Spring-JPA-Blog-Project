package myBlog.core.service;

import lombok.RequiredArgsConstructor;
import myBlog.core.Domain.Board;
import myBlog.core.Domain.User;
import myBlog.core.dto.BoardUpdateDto;
import myBlog.core.dto.NewBoardDto;
import myBlog.core.dto.ResponseDto;
import myBlog.core.exception.BoardNotFoundException;
import myBlog.core.repository.BoardRepository;
import myBlog.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public Long save(NewBoardDto newBoardDto) throws UsernameNotFoundException {
        // 글쓴 유저 객체 리턴
        Optional<User> user = userRepository.findById(newBoardDto.getUserId());
        user.orElseThrow(()-> {
            throw new UsernameNotFoundException("boardServiceImpl : save => do not found user Entity");
        });

        // board entity 생성
        Board board = new Board(newBoardDto.getContent() , newBoardDto.getTitle());
        board.setUser(user.get());
        // 처음 조회수 default 0으로 설정

        board.setCount(0);
        // board 영속성 컨텍스트에 저장
        Board storedBoard = boardRepository.save(board);

        return storedBoard.getBoardId();
    }

    @Override
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Override
    public Board findById(Long boardId) throws BoardNotFoundException {
        Optional<Board> board = boardRepository.findById(boardId);
        board.orElseThrow(()-> {
            throw new BoardNotFoundException("BoardService : findById => do not found board detail page resource");
        });

        return board.get();
    }

    @Override
    @Transactional
    public void viewCountUp(Long boardId) throws BoardNotFoundException {
        Optional<Board> board = boardRepository.findById(boardId);
        board.orElseThrow( ()-> {
            throw new BoardNotFoundException("BoardService : viewCountUp => do not found board entiy!");
        });

        Board boardEntity = board.get();
        boardEntity.setCount(boardEntity.getCount()+1);
    }

    @Override
    @Transactional
    public void updateById(BoardUpdateDto boardUpdateDto) throws BoardNotFoundException {
        Board board = this.findById(boardUpdateDto.getBoardId());
        // 게시물 업데이트 내용 Dirty checking
        try {
            update(board , boardUpdateDto);
        } catch (Exception e){
            System.out.println("boardService : updateById : e = " + e);
            e.printStackTrace();
        }
    }

    private void update(Board board, BoardUpdateDto boardUpdateDto){
        board.setTitle(boardUpdateDto.getTitle());
        board.setContent(boardUpdateDto.getContent());
    }
}


