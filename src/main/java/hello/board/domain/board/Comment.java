package hello.board.domain.board;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String commentWriter;
    private String commentContent;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Board board;


    protected Comment() {
    }

    public Comment(String commentWriter, String commentContent, Board board) {
        this.commentWriter = commentWriter;
        this.commentContent = commentContent;
        this.board = board;
    }
}
