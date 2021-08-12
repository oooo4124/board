package hello.board.web.controller;

import hello.board.domain.member.Member;
import hello.board.domain.post.Post;
import hello.board.domain.post.Comment;
import hello.board.service.PostService;
import hello.board.service.CommentService;
import hello.board.web.SessionConst;
import hello.board.web.controller.form.CommentForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    // 댓글 작성
    @PostMapping("board/{postId}/comment")
    public String commentWrite(@PathVariable("postId") Long postId,
                               @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                               CommentForm form,
                               HttpServletRequest request) throws IllegalAccessException{

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Post findPost = postService.findPost(postId);
        Comment comment = new Comment(loginMember.getUsername(), form.getCommentContent(), findPost);
        commentService.saveComment(comment);
        log.info("댓글 ={}",comment);



        return "redirect:/board/{postId}/postView";

    }
}
