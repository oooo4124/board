package hello.board.repository;

import hello.board.domain.member.Member;
import hello.board.domain.post.Post;
import hello.board.dto.PostDto;
import hello.board.web.controller.BoardSearch;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager em;

    @Test
    public void CRUD() {
        Member member1 = new Member("test1", "test", "member1");
        Member member2 = new Member("test2", "test", "member2");
        em.persist(member1);
        em.persist(member2);
        Post post1 = new Post("post1", "post1", member1, member1.getUsername(), LocalDateTime.now(), 0, null);
        Post post2 = new Post("post2", "post2", member2, member2.getUsername(), LocalDateTime.now(), 0, null);
        postRepository.save(post1);
        postRepository.save(post2);

        //단건 조회
        Post findPost = postRepository.findById(post1.getId()).get();
        assertThat(findPost).isEqualTo(post1);

        //페이징 조회
        String searchCondition = "postTitle";
        String searchContent = "post1";
        BoardSearch boardSearch = new BoardSearch();
        boardSearch.setSearchCondition(searchCondition);
        boardSearch.setSearchContent(searchContent);

        Pageable pageable = PageRequest.of(0, 5);
        Page<PostDto> postDtos = postRepository.boardSearch(boardSearch, pageable);
        assertThat(postDtos.getContent().get(0).getId()).isEqualTo(post1.getId());

        //수정
        String postContent = "post3";
        post1.update(postContent);
        assertThat(post1.getPostContent()).isEqualTo(postContent);

        //삭제
        postRepository.delete(post1);
        assertThat(postRepository.count()).isEqualTo(1);

    }




}