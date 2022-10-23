package com.example.lems;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class BoardListViewAdapter extends BaseAdapter{

    private DatabaseReference mDatabase;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<PostDTO> listViewItemList = new ArrayList<PostDTO>() ;

    // ListViewAdapter의 생성자
    public BoardListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position : 리턴 할 자식 뷰의 위치
    // convertView : 메소드 호출 시 position에 위치하는 자식 뷰 ( if == null 자식뷰 생성 )
    // parent : 리턴할 부모 뷰, 어댑터 뷰
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BoardListViewItem listView = null;

        //final int pos = position;
        //final Context context = parent.getContext();

        //mDatabase = FirebaseDatabase.getInstance().getReference();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_list_listview, parent, false);*/
            //listView = new BoardListViewItem(BoardListViewAdapter.this);

        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        //TextView descTextView = (TextView) convertView.findViewById(R.id.textView2) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        //BoardListViewItem listViewItem = listViewItemList.get(position);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수.
    public void addItem(Drawable icon, String title, String desc) {
        //BoardListViewItem item = new BoardListViewItem();

        /*item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);*/



        //listViewItemList.add(item);
    }

    // 아이템 데이터 추가를 위한 함수. ( Test 용 )
    public void addItem(PostDTO item) {
        listViewItemList.add(item);
        //BoardListViewItem item = new BoardListViewItem();
        //listViewItemList.add(item);
    }
}

