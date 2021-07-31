package hello.board.web.board;

import hello.board.domain.member.Member;
import hello.board.domain.board.Board;
import hello.board.domain.board.Comment;
import hello.board.service.BoardService;
import hello.board.service.CommentService;
import hello.board.web.SessionConst;
import hello.board.web.board.form.CommentForm;
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
    private final BoardService boardService;

    // 댓글 작성
    @PostMapping("board/{postId}/comment")
    public String commentWrite(@PathVariable("postId") Long postId,
                               @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                               CommentForm form,
                               HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        Board findBoard = boardService.findOne(postId);
        Comment comment = new Comment(loginMember.getName(), form.getCommentContent(), findBoard);
        commentService.saveComment(comment);
        log.info("댓글 ={}",comment);



        return "redirect:/board/{postId}/postView";

    }
}
