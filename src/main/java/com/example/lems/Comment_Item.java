package com.example.lems;

public class Comment_Item {
    private String content, nickname, date;
    public Comment_Item(){
        this.content = null;
        this.nickname = null;
        this.date = null;
    }
    public void setContent(String c){
        this.content = c;
    }
    public String getContent(){
        return this.content;
    }
    public void setNickname(String c){
        this.nickname = c;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setDate(String d) { this.date = d; }
    public String getDate() { return this.date; }
}
