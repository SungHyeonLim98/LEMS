package com.example.lems;

public class LecturePostDTO {

    public String Uid;
    public String title;
    public String contents;
    public String date;
    public String commentCount;
    public String suggestionCount;
    public String department;
    public String professor;
    public String lecture;

    LecturePostDTO() {

    }
    LecturePostDTO(String title, String contents, String date) {
        this.Uid = null;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = "0";
        this.suggestionCount = "0";
        this.professor = null;
        this.lecture = null;
    }

    LecturePostDTO(String Uid, String title, String contents, String date) {
        this.Uid = Uid;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = "0";
        this.suggestionCount = "0";
        this.professor = null;
        this.lecture = null;
    }
    LecturePostDTO(String Uid, String title, String contents, String date, String commentCount, String suggestionCount) {
        this.Uid = Uid;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = commentCount;
        this.suggestionCount = suggestionCount;
        this.professor = null;
        this.lecture = null;
    }
    LecturePostDTO(String Uid, String title, String contents, String date, String commentCount, String suggestionCount,
                   String department, String professor, String lecture) {
        this.Uid = Uid;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = commentCount;
        this.suggestionCount = suggestionCount;
        this.department = department;
        this.professor = professor;
        this.lecture = lecture;
    }
    LecturePostDTO(String Uid, String title, String contents, String date,
                   String department, String professor, String lecture) {
        this.Uid = Uid;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.commentCount = "0";
        this.suggestionCount = "0";
        this.department = department;
        this.professor = professor;
        this.lecture = lecture;
    }

    public String getCommentCount() { return commentCount; }
    public String getSuggestionCount() { return suggestionCount; }
}
