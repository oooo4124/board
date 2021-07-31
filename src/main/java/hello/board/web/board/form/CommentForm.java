package hello.board.web.board.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CommentForm {

    @NotBlank
    private String commentContent;
}
