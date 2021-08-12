package hello.board.service;

import hello.board.domain.post.Post;
import hello.board.web.controller.BoardSearch;
import hello.board.dto.PostDto;
import hello.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Long savePost(Post post) {
        return postRepository.save(post).getId();
    }

    @Transactional
    public void updatePost(Long postId, String postContent) throws IllegalAccessException {
        Post findPost = postRepository.findById(postId).orElseThrow(()-> new IllegalAccessException("잘못된 접근입니다."));
        findPost.update(postContent);
    }

    public Page<PostDto> search(BoardSearch boardSearch, Pageable pageable) {

        // 검색조건이 들어왔는지 체크해서 값 설정
        boardSearch.check();

        return postRepository.boardSearch(boardSearch, pageable);
    }

    // 조회수 업
    @Transactional
    public Post upViews(Long id) throws IllegalAccessException {

        Post findPost = postRepository.findById(id).orElseThrow(()-> new IllegalAccessException("잘못된 접근입니다."));
        findPost.addViewCount();

        return findPost;
    }

    //게시물 삭제
    @Transactional
    public void deletePost(Long postId) throws IllegalAccessException {
        Post findPost = postRepository.findById(postId).orElseThrow(()-> new IllegalAccessException("잘못된 접근입니다."));
        postRepository.delete(findPost);
    }

    public Post findPost(Long postId) throws IllegalAccessException {
        return postRepository.findById(postId).orElseThrow(()-> new IllegalAccessException("잘못된 접근입니다."));
    }
}
