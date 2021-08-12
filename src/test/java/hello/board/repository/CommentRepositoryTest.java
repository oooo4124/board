package hello.board.repository;

import hello.board.domain.member.Member;
import hello.board.domain.post.Comment;
import hello.board.domain.post.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
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
        em.persist(post1);
        em.persist(post2);

        Comment comment1 = new Comment(post1.getWriter(), "teat1", post1);
        Comment comment2 = new Comment(post1.getWriter(), "teat2", post1);
        Comment comment3 = new Comment(post1.getWriter(), "teat3", post2);
        Comment comment4 = new Comment(post1.getWriter(), "teat4", post2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);


        //postId로 조회 post1의 댓글 수와 작성자
        List<Comment> findCommentList = commentRepository.findByPostId(post1.getId());
        assertThat(findCommentList.size()).isEqualTo(2);
        assertThat(findCommentList).extracting("commentWriter").containsExactly("member1","member1");

        //삭제
        commentRepository.delete(comment1);
        commentRepository.delete(comment2);
        long delCount = commentRepository.count();
        assertThat(delCount).isEqualTo(2);
    }

}