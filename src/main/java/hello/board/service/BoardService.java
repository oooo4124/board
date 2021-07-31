package hello.board.service;

import hello.board.domain.board.Board;
import hello.board.domain.board.Comment;
import hello.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long savePost(Board board) {
        return boardRepository.save(board);
    }

    @Transactional
    public void updatePost(Long postId, String postContent) {
        Board findPost = boardRepository.findOne(postId);
        findPost.update(postContent);
    }

    public List<Board> findBoards() {
        return boardRepository.findAll();
    }


    public Board findOne(Long boardId) {
        return boardRepository.findOne(boardId);
    }

    @Transactional
    public Board upViews(Long id) {

        Board findPost = boardRepository.findOne(id);
        findPost.addViewCount();

        return findPost;
    }

    @Transactional
    public void deletePost(Long postId) {
        Board findPost = boardRepository.findOne(postId);
        boardRepository.deletePost(findPost);
    }
}
