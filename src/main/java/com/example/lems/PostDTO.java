package com.example.lems;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PostDTO {
    public String Uid;
    public String title;
    public String contents;
    public String date;
    public String commentCount;
    public String suggestionCount;

    PostDTO() {

    }
    PostDTO(String title, String contents, String date) {
        this.Uid = null;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = "댓글 0";
        this.suggestionCount = "추천 0";
    }

    PostDTO(String Uid, String title, String contents, String date) {
        this.Uid = Uid;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = "0";
        this.suggestionCount = "0";
    }
    PostDTO(String Uid, String title, String contents, String date, String commentCount, String suggestionCount) {
        this.Uid = Uid;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = commentCount;
        this.suggestionCount = suggestionCount;
    }

    public String getCommentCount() { return commentCount; }
    public String getSuggestionCount() { return suggestionCount; }
}
