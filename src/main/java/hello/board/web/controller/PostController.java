package hello.board.web.controller;

import hello.board.domain.post.Post;
import hello.board.domain.fileUpload.UploadFile;
import hello.board.domain.member.Member;
import hello.board.domain.post.Comment;
import hello.board.dto.PostDto;
import hello.board.file.FileStore;
import hello.board.service.PostService;
import hello.board.service.CommentService;
import hello.board.web.SessionConst;
import hello.board.web.controller.form.CommentForm;
import hello.board.web.controller.form.PostForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final FileStore fileStore;

    //게시판 메인 리스트
    @GetMapping(value = {"","board"})
    public String boardMain(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            BoardSearch boardSearch, Model model, Pageable pageable) {

        Page<PostDto> posts = postService.search(boardSearch, pageable);


        model.addAttribute("boardSearch", boardSearch);
        model.addAttribute("member", loginMember);
        model.addAttribute("posts", posts);

        return "mainForm";
    }

    // 게시글 작성 폼
    @GetMapping("board/newPost")
    public String newPost(Model model) {

        model.addAttribute("form", new PostForm());
        return "board/newPostForm";
    }

    // 게시글 작성
    @PostMapping("board/newPost")
    public String post(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       @ModelAttribute PostForm form) throws IOException {

        List<MultipartFile> multipartFiles = form.getMultipartFiles();
        List<UploadFile> uploadFiles = fileStore.storeFiles(multipartFiles);


        Post board =
                new Post(form.getPostTitle(), form.getPostContent(), loginMember, loginMember.getUsername(),
                        LocalDateTime.now(), 0, uploadFiles);


        Long postId = postService.savePost(board);
        return "redirect:/board/" + postId + "/postView";
    }

    //게시글 조회
    @GetMapping("board/{postId}/postView")
    public String postView(@PathVariable("postId") Long postId,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                           Model model) throws IllegalAccessException {
        Post findPost = postService.findPost(postId);

        //      자신의 글이 아닐 경우 조회수 업
        if (findPost.getMember().getLoginId() != loginMember.getLoginId()) {
            postService.upViews(postId);
            model.addAttribute("loginMember", loginMember);
        }
        List<Comment> comments = commentService.findComments(findPost.getId());
        log.info("comments ={}", comments);

        model.addAttribute("findPost", findPost);
        model.addAttribute("form", new CommentForm());
        model.addAttribute("comments", comments);
        return "board/postViewForm";
    }

    // 게시글 수정 폼
    @GetMapping("board/{postId}/updatePost")
    public String updatePost(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                 @PathVariable("postId") Long postId, Model model) throws IllegalAccessException {

        Post findPost = postService.findPost(postId);

        //작성자와 로그인한 사용자가 다를 경우 메인으로
        if (loginMember.getLoginId() != findPost.getMember().getLoginId()) {
            return "redirect:/";
        }

        model.addAttribute("findPost", findPost);
        model.addAttribute("form", new PostForm());
        return "board/updatePostForm";
    }

    // 게시글 수정
    @PostMapping("board/{postId}/updatePost")
    public String updatePostComp(@PathVariable("postId") Long postId, @ModelAttribute PostForm form) throws IllegalAccessException {

        postService.updatePost(postId, form.getPostContent());

        return "redirect:/board/{postId}/postView";
    }

    // 게시글 삭제
    @GetMapping("board/{postId}/deletePost")
    public String deletePost(@PathVariable("postId") Long postId) throws IllegalAccessException {

        postService.deletePost(postId);

        return "redirect:/";
    }

    //  첨부파일 다운로드
    @GetMapping("attach/{postId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long postId,
                                                    @RequestParam String storeFileName) throws MalformedURLException, IllegalAccessException {

        String uploadFileName = null;

        Post findPost = postService.findPost(postId);
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
