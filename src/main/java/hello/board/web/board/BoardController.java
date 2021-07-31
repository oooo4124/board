package hello.board.web.board;

import hello.board.domain.board.Board;
import hello.board.domain.fileUpload.UploadFile;
import hello.board.domain.member.Member;
import hello.board.domain.board.Comment;
import hello.board.file.FileStore;
import hello.board.service.BoardService;
import hello.board.service.CommentService;
import hello.board.web.SessionConst;
import hello.board.web.board.form.BoardForm;
import hello.board.web.board.form.CommentForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final FileStore fileStore;

    @GetMapping("/board")
    public String board() {
        return "mainForm";
    }

    @GetMapping("/")
    public String boardLogin(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        List<Board> posts = boardService.findBoards();
        model.addAttribute("member", loginMember);
        model.addAttribute("posts", posts);

        return "mainForm";
    }

    // 게시글 작성 폼
    @GetMapping("/board/newPost")
    public String newPost(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }
        model.addAttribute("form", new BoardForm());
        return "board/newPostForm";
    }

    // 게시글 작성
    @PostMapping("/board/newPost")
    public String post(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       @ModelAttribute BoardForm form) throws IOException {

        List<MultipartFile> multipartFiles = form.getMultipartFiles();
        List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);


        Board board =
                new Board(form.getPostTitle(), form.getPostContent(), loginMember,
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM.dd. hh:mm")), 0, uploadFiles);


        Long postId = boardService.savePost(board);
        return "redirect:/board/" + postId + "/postView";
    }

    //게시글 조회
    @GetMapping("/board/{postId}/postView")
    public String postView(@PathVariable("postId") Long postId,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           Model model) {
        Board findPost = boardService.findOne(postId);

        if (loginMember == null) {
            return "redirect:/login";
        }
        //      자신의 글이 아닐 경우 조회수 업
        if (findPost.getMember().getLoginId() != loginMember.getLoginId()) {
            boardService.upViews(postId);
            model.addAttribute("loginMember", loginMember);
        }
        List<Comment> comments = commentService.findComments(findPost);
        log.info("comments ={}", comments);

        model.addAttribute("findPost", findPost);
        model.addAttribute("form", new CommentForm());
        model.addAttribute("comments", comments);
        return "board/postViewForm";
    }

    // 게시글 수정 폼
    @GetMapping("/board/{postId}/updatePost")
    public String updatePost(@PathVariable("postId") Long postId, Model model) {
        Board findPost = boardService.findOne(postId);

        model.addAttribute("findPost", findPost);
        model.addAttribute("form", new BoardForm());
        return "board/updatePostForm";
    }

    // 게시글 수정
    @PostMapping("/board/{postId}/updatePost")
    public String updatePostComp(@PathVariable("postId") Long postId, @ModelAttribute BoardForm form) {

        boardService.updatePost(postId, form.getPostContent());

        return "redirect:/board/{postId}/postView";
    }

    // 게시글 삭제
    @GetMapping("/board/{postId}/deletePost")
    public String deletePost(@PathVariable("postId") Long postId) {

        boardService.deletePost(postId);

        return "redirect:/";
    }

    //  첨부파일 다운로드
    @GetMapping("/attach/{postId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long postId,
                                                    @RequestParam String storeFileName) throws MalformedURLException {

        String uploadFileName = null;

        Board findPost = boardService.findOne(postId);
        List<UploadFile> uploadFiles = findPost.getUploadFiles();
        for (UploadFile uploadFile : uploadFiles) {
            if (uploadFile.getStoreFileName().equals(storeFileName)) {
                uploadFileName = uploadFile.getUploadFileName();
            }
        }

        if (uploadFileName == null) {
            return null;
        }

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        String encodedUploadFilename = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFilename + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
