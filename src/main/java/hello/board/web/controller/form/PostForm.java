package hello.board.web.controller.form;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
public class PostForm {


    @NotBlank
    private String postTitle;
    @NotBlank
    private String postContent;

    private List<MultipartFile> multipartFiles;

}
