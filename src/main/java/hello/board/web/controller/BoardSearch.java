package hello.board.web.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BoardSearch {

    private String searchCondition;
    private String searchContent;

    //검색 조건 작성자, 글 제목, 글 내용
    private String username;
    private String postTitle;
    private String postContent;

    public void check() {
        if (this.getSearchCondition() == null){
            this.setPostTitle("");
            this.setPostContent("");
            this.setUsername("");
        }
        else if (this.getSearchCondition().equals("postTitle")) {
            this.setPostTitle(this.getSearchContent());
        }
        else if (this.getSearchCondition().equals("postContent")) {
            this.setPostContent(this.getSearchContent());
        }
        else if (this.getSearchCondition().equals("username")) {
            this.setUsername(this.getSearchContent());
        }
        else if (this.getSearchCondition().equals("all")){
            this.setSearchContent(this.getSearchContent());
        }

    }
}
