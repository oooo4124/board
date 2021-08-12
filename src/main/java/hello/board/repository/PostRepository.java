package hello.board.repository;

import hello.board.domain.post.Post;
import hello.board.web.controller.BoardSearch;
import hello.board.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Override
    Page<PostDto> boardSearch(BoardSearch boardSearch, Pageable pageable);
}
