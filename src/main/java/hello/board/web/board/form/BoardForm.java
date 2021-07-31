package hello.board.web.board.form;

import hello.board.domain.fileUpload.UploadFile;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
public class BoardForm {


    @NotBlank
    private String postTitle;
    @NotBlank
    private String postContent;

    private List<MultipartFile> multipartFiles;

}
