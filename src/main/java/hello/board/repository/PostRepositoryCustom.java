package hello.board.repository;

import hello.board.web.controller.BoardSearch;
import hello.board.dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


//
public interface PostRepositoryCustom {

    Page<PostDto> boardSearch(BoardSearch condition, Pageable pageable);
}
