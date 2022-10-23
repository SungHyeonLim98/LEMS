package com.example.lems;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.jar.Attributes;

public class BoardListViewItem extends LinearLayout {

    TextView titleText, contentsText, dateText, suggestion, comment;
    LinearLayout listArea;

    public BoardListViewItem(Context context) {
        super(context);
        init(context);
    }

    public BoardListViewItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.board_list_listview, this, true);

        titleText = findViewById(R.id.title);
        contentsText = findViewById(R.id.contents);
        dateText = findViewById(R.id.date);
        listArea = findViewById(R.id.listArea);
        suggestion = findViewById(R.id.ddabongText);
        comment = findViewById(R.id.commentText);
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }
    public void setContents(String contents) {
        contentsText.setText(contents);
    }
    public void setDate(String date) { dateText.setText(date); }
    public void setCommentCount(String comment) { this.comment.setText(comment); }
    public void setSuggestionCount(String suggestion) { this.suggestion.setText(suggestion);}
}
