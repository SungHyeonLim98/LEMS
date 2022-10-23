package com.example.lems;

public class CommentDTO {
    public String postName;
    public String userUid;
    public String contents;
    public String date;

    CommentDTO() {

    }
    CommentDTO(String postName, String userUid, String contents, String date) {
        this.postName = postName;
        this.userUid = userUid;
        this.contents = contents;
        this.date = date;
    }
}
