package hello.board.repository;

import hello.board.domain.board.Board;
import hello.board.domain.board.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }


    public List<Comment> findBypostId(Board board) {
        return em.createQuery("select c from Comment c where c.board = :board", Comment.class)
                .setParameter("board", board)
                .getResultList();
    }
}
