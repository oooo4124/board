package hello.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String postTitle;
    private String writer;
    private LocalDateTime postDate;
    private int viewCount;
    private Integer commentCount;
    private Long memberId;
    private String userName;

    @QueryProjection
    public PostDto(Long id, String postTitle, LocalDateTime postDate, int viewCount, int commentCount, Long memberId, String userName) {
        this.id = id;
        this.postTitle = postTitle;
        this.postDate = postDate;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.memberId = memberId;
        this.userName = userName;
    }
}
