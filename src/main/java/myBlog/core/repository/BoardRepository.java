package myBlog.core.repository;

import myBlog.core.Domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board , Long> {
    // 전체 게시글 페이징 처리 후 리턴
    Page<Board> findAll(Pageable pageable);

    Optional<Board> findById(Long boardId);
}