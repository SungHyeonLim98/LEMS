package com.example.lems;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BoardListSugg extends AppCompatActivity {

    //private BoardListViewAdapter adapter;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;

    //private ArrayAdapter adapter = new ArrayAdapter(this, R.layout.board_list_listview);

    ImageButton board, mypage, lecture;
    ImageButton enroll;
    Button nextBtn;
    SearchView searchView;

    static String comm, sugg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list_sugg);

        board = findViewById(R.id.post);
        mypage = findViewById(R.id.mypage);
        lecture = findViewById(R.id.lecture);
        enroll = findViewById(R.id.Enroll);
        searchView = findViewById(R.id.searchView);
        nextBtn = findViewById(R.id.btn1);

        ListView listView = findViewById(R.id.listView);
        BoardListViewAdapter adapter = new BoardListViewAdapter();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();


        ArrayList<PostDTO> postArray = new ArrayList<PostDTO>();

        mDatabase.child("Post").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "실패",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String values = String.valueOf(task.getResult().getValue());
                    String[] result = values.split("date=|Uid=|contents=|title=");
                    String titleStr, contentsStr, dateStr, uidStr, commStr, suggStr;

                    if (result.length <= 5 && result.length > 0) {
                        titleStr = result[4].substring(0, result[4].lastIndexOf("},"));
                        contentsStr = result[3].substring(0, result[3].length() - 2);
                        dateStr = result[1].substring(0, result[1].length() - 2);


                        postArray.add(new PostDTO(titleStr, contentsStr, dateStr));

                    } else {
                        for (int i = 0; i < result.length - 1; i += 4) {
                            if (i + 5 >= result.length)
                                titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("}}"));
                            else {
                                titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("},"));
                                contentsStr = result[i + 3].substring(0, result[i + 3].length() - 2);
                                dateStr = result[i + 1].substring(0, result[i + 1].length() - 2);
                                uidStr = result[i].substring(0, result[i].length() - 2);

                                if (titleStr.contains(", commentCount=")) {
                                    if(!titleStr.substring(titleStr.indexOf("=")+1, titleStr.indexOf("=")+2).equals("댓")) {
                                        commStr = "댓글 " + titleStr.substring(titleStr.indexOf("=") + 1, titleStr.indexOf("=") + 2);
                                    } else {
                                        commStr = "댓글 " + titleStr.substring(titleStr.indexOf("글") + 2, titleStr.indexOf("글") + 3);
                                    }
                                    titleStr = titleStr.substring(0, titleStr.indexOf(", commentCount="));
                                } else {
                                    commStr = "댓글 0";
                                }
                                if(contentsStr.contains(", suggestionCount=")) {
                                    if(!contentsStr.substring(contentsStr.indexOf("=")+1, contentsStr.indexOf("=")+2).equals("추")) {
                                        suggStr = "추천 " + contentsStr.substring(contentsStr.indexOf("=")+1, contentsStr.indexOf("=")+2);
                                    } else {
                                        suggStr = "추천 " + contentsStr.substring(contentsStr.indexOf("천") + 2, contentsStr.indexOf("천") + 3);
                                    }
                                    contentsStr = contentsStr.substring(0, contentsStr.indexOf(", suggestionCount="));
                                } else {
                                    suggStr = "추천 0";
                                }
                                if (uidStr.contains("{")) {
                                    uidStr = " " + uidStr.substring(uidStr.indexOf("{") + 1, uidStr.length());
                                }
                                if (uidStr.contains(",")) {
                                    uidStr = uidStr.substring(uidStr.indexOf(",") + 1, uidStr.length());
                                }

                                postArray.add(new PostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr));
                            }
                        }
                    }

                    //Comparator 를 만든다.
                    Comparator<PostDTO> myComparator = new Comparator<PostDTO>() {
                        @Override
                        public int compare(PostDTO post1, PostDTO post2) {
                            return post2.suggestionCount.compareTo(post1.suggestionCount);
                        }
                    };

                    Collections.sort(postArray, myComparator);

                    for (int i = 0; i < postArray.size(); i++)
                        adapter.addItem(postArray.get(i));

                    listView.setAdapter(adapter);
                }
            }
        });

        nextBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), BoardList.class);
                startActivity(intent);

                finish();
                return false;
            }
        });

        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(getApplicationContext(), ListSearchView.class);
                intent.putExtra("text", query);
                startActivity(intent);

                finish();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });


        enroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), Board.class);
                startActivity(intent);

                finish();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Board.class);
                startActivity(intent);

                finish();
            }
        });

        mypage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent);

                finish();

                return false;
            }
        });

        lecture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), LectureList.class);
                startActivity(intent);

                finish();
                return false;
            }
        });
    }

    public class BoardListViewAdapter extends BaseAdapter {

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

            if (convertView == null) {
                listView = new BoardListViewItem(getApplicationContext());
            } else {
                listView = (BoardListViewItem)convertView;
            }
            PostDTO post = listViewItemList.get(position);
            listView.setTitle(post.title);
            listView.setContents(post.contents);
            listView.setDate(post.date);
            listView.setSuggestionCount(post.suggestionCount);
            listView.setCommentCount(post.commentCount);

            listView.listArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Post.class);
                    intent.putExtra("title", post.title);
                    intent.putExtra("contents", post.contents);
                    intent.putExtra("date", post.date);
                    intent.putExtra("Uid", post.Uid);
                    intent.putExtra("comm", post.commentCount);
                    intent.putExtra("sugg", post.suggestionCount);

                    startActivity(intent);

                    finish();
                }
            });
            return listView;

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

        public void addItem(String title, String contents, String date) {
            PostDTO post = new PostDTO();
            post.contents = contents;
            post.title = title;
            post.date = date;
            listViewItemList.add(post);
        }

        public void addItem(PostDTO item) {
            listViewItemList.add(item);
        }

        public void removeItem(Object item) {
            listViewItemList.remove(item);
        }


    }
}