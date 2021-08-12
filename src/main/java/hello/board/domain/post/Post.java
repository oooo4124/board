package hello.board.domain.post;

import hello.board.domain.fileUpload.UploadFile;
import hello.board.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @NotEmpty
    private String postTitle;
    @NotEmpty
    private String postContent;
    private String writer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime postDate;
    private int viewCount;

    @ElementCollection
    private List<UploadFile> uploadFiles;

    public Post(String postTitle, String postContent, Member member, String writer, LocalDateTime postDate, Integer viewCount, List<UploadFile> uploadFiles) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.member = member;
        this.writer = member.getUsername();
        this.postDate = postDate;
        this.viewCount = viewCount;
        this.uploadFiles = uploadFiles;
    }

    protected Post() {
    }


    // 비즈니스 로직

    /**
     * 조회수 증가
     */
    public void addViewCount() {
        viewCount += 1;
    }

    public void update(String postContent) {
        this.postContent = postContent;
    }

}
