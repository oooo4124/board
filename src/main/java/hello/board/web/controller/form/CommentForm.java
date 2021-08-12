package hello.board.web.controller.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CommentForm {

    @NotBlank
    private String commentContent;
}
