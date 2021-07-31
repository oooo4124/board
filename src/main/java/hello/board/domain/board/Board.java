package hello.board.domain.board;

import hello.board.domain.fileUpload.UploadFile;
import hello.board.domain.member.Member;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @NotEmpty
    private String postTitle;
    @NotEmpty
    private String postContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_loginId")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    private String postDate;
    private int viewCount;

    @ElementCollection
    private List<UploadFile> uploadFiles;

    public Board(String postTitle, String postContent, Member member, String postDate, Integer viewCount, List<UploadFile> uploadFiles) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.member = member;
        this.postDate = postDate;
        this.viewCount = viewCount;
        this.uploadFiles = uploadFiles;
    }

    protected Board() {
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
