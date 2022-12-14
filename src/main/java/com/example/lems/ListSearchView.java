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

public class ListSearchView extends AppCompatActivity {

    //private BoardListViewAdapter adapter;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;

    //private ArrayAdapter adapter = new ArrayAdapter(this, R.layout.board_list_listview);

    ImageButton board, mypage, lecture;
    ImageButton enroll;
    SearchView searchView;

    static String comm, sugg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_search_view);

        board = findViewById(R.id.post);
        mypage = findViewById(R.id.mypage);
        lecture = findViewById(R.id.lecture);
        enroll = findViewById(R.id.Enroll);
        searchView = findViewById(R.id.searchView);

        ListView listView = findViewById(R.id.listView);
        BoardListViewAdapter adapter = new BoardListViewAdapter();
        LectureListViewAdapter ladapter = new LectureListViewAdapter();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();

        ArrayList<PostDTO> postArray = new ArrayList<PostDTO>();
        ArrayList<LecturePostDTO> lecturePostArray = new ArrayList<LecturePostDTO>();

        if(intent.getStringExtra("lecture").equals("lecture")) {
            mDatabase.child("LecturePost").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "??????",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String values = String.valueOf(task.getResult().getValue());
                        String[] result = values.split("date=|Uid=|contents=|title=");
                        String titleStr, contentsStr, dateStr, uidStr, commStr, suggStr, proStr, departStr, lecStr;

                        if (result.length <= 5 && result.length > 0) {
                            titleStr = result[4].substring(0, result[4].lastIndexOf("},"));
                            contentsStr = result[3].substring(0, result[3].length() - 2);
                            dateStr = result[1].substring(0, result[1].length() - 2);

                            return;
                        } else {
                            for (int i = 0; i < result.length - 1; i += 4) {
                                if (i + 5 >= result.length)
                                    titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("}}"));
                                else {
                                    titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("},"));
                                    contentsStr = result[i + 3].substring(0, result[i + 3].length() - 2);
                                    dateStr = result[i + 1].substring(0, result[i + 1].length() - 2);
                                    uidStr = result[i].substring(0, result[i].length() - 2);
                                    proStr = result[i+2].substring(result[i+2].indexOf("=")+1, result[i+2].length() - 2);

                                    if (titleStr.contains(", commentCount=")) {
                                        if(!titleStr.substring(titleStr.indexOf("=")+1, titleStr.indexOf("=")+2).equals("???")) {
                                            commStr = "?????? " + titleStr.substring(titleStr.indexOf("=") + 1, titleStr.indexOf("=") + 2);
                                        } else {
                                            commStr = "?????? " + titleStr.substring(titleStr.indexOf("???") + 2, titleStr.indexOf("???") + 3);
                                        }
                                        titleStr = titleStr.substring(0, titleStr.indexOf(", commentCount="));
                                    } else {
                                        commStr = "?????? 0";
                                    }
                                    if(contentsStr.contains(", suggestionCount=")) {
                                        if(!contentsStr.substring(contentsStr.indexOf("=")+1, contentsStr.indexOf("=")+2).equals("???")) {
                                            suggStr = "?????? " + contentsStr.substring(contentsStr.indexOf("=")+1, contentsStr.indexOf("=")+2);
                                        } else {
                                            suggStr = "?????? " + contentsStr.substring(contentsStr.indexOf("???") + 2, contentsStr.indexOf("???") + 3);
                                        }
                                    } else {
                                        suggStr = "?????? 0";
                                    }
                                    if (uidStr.contains("{")) {
                                        uidStr = " " + uidStr.substring(uidStr.indexOf("{") + 1, uidStr.length());
                                    }
                                    if (uidStr.contains(",")) {
                                        uidStr = uidStr.substring(uidStr.indexOf(",") + 1, uidStr.length());
                                    }

                                    departStr = contentsStr.substring(contentsStr.indexOf("department=")+11);
                                    lecStr = contentsStr.substring(contentsStr.indexOf("lecture=")+8, contentsStr.indexOf(", department"));
                                    contentsStr = contentsStr.substring(0, contentsStr.indexOf(", suggestionCount="));

                                    if(intent.getStringExtra("text").contains(titleStr)) {
                                        lecturePostArray.add(new LecturePostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr,
                                                departStr, proStr, lecStr));
                                    }
                                }
                            }
                        }

                        if (lecturePostArray.size() > 0) {
                            //Comparator ??? ?????????.
                            Comparator<LecturePostDTO> myComparator = new Comparator<LecturePostDTO>() {
                                @Override
                                public int compare(LecturePostDTO post1, LecturePostDTO post2) {
                                    return post2.date.compareTo(post1.date);
                                }
                            };

                            Collections.sort(lecturePostArray, myComparator);

                            for (int i = 0; i < lecturePostArray.size(); i++)
                                ladapter.addItem(lecturePostArray.get(i));

                            listView.setAdapter(ladapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "?????? ????????? ????????????.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LectureList.class);
                            startActivity(i);

                            finish();
                        }
                    }
                }
            });
        } else if(intent.getStringExtra("lecture").equals("lectureSpinner")) {
            mDatabase.child("LecturePost").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "??????",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String values = String.valueOf(task.getResult().getValue());
                        String[] result = values.split("date=|Uid=|contents=|title=");
                        String titleStr, contentsStr, dateStr, uidStr, commStr, suggStr, proStr, departStr, lecStr;

                        if (result.length <= 5 && result.length > 0) {
                            titleStr = result[4].substring(0, result[4].lastIndexOf("},"));
                            contentsStr = result[3].substring(0, result[3].length() - 2);
                            dateStr = result[1].substring(0, result[1].length() - 2);

                            return;
                        } else {
                            for (int i = 0; i < result.length - 1; i += 4) {
                                if (i + 5 >= result.length)
                                    titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("}}"));
                                else {
                                    titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("},"));
                                    contentsStr = result[i + 3].substring(0, result[i + 3].length() - 2);
                                    dateStr = result[i + 1].substring(0, result[i + 1].length() - 2);
                                    uidStr = result[i].substring(0, result[i].length() - 2);
                                    proStr = result[i+2].substring(result[i+2].indexOf("=")+1, result[i+2].length() - 2);

                                    if (titleStr.contains(", commentCount=")) {
                                        if(!titleStr.substring(titleStr.indexOf("=")+1, titleStr.indexOf("=")+2).equals("???")) {
                                            commStr = "?????? " + titleStr.substring(titleStr.indexOf("=") + 1, titleStr.indexOf("=") + 2);
                                        } else {
                                            commStr = "?????? " + titleStr.substring(titleStr.indexOf("???") + 2, titleStr.indexOf("???") + 3);
                                        }
                                        titleStr = titleStr.substring(0, titleStr.indexOf(", commentCount="));
                                    } else {
                                        commStr = "?????? 0";
                                    }
                                    if(contentsStr.contains(", suggestionCount=")) {
                                        if(!contentsStr.substring(contentsStr.indexOf("=")+1, contentsStr.indexOf("=")+2).equals("???")) {
                                            suggStr = "?????? " + contentsStr.substring(contentsStr.indexOf("=")+1, contentsStr.indexOf("=")+2);
                                        } else {
                                            suggStr = "?????? " + contentsStr.substring(contentsStr.indexOf("???") + 2, contentsStr.indexOf("???") + 3);
                                        }
                                    } else {
                                        suggStr = "?????? 0";
                                    }
                                    if (uidStr.contains("{")) {
                                        uidStr = " " + uidStr.substring(uidStr.indexOf("{") + 1, uidStr.length());
                                    }
                                    if (uidStr.contains(",")) {
                                        uidStr = uidStr.substring(uidStr.indexOf(",") + 1, uidStr.length());
                                    }

                                    departStr = contentsStr.substring(contentsStr.indexOf("department=")+11);
                                    lecStr = contentsStr.substring(contentsStr.indexOf("lecture=")+8, contentsStr.indexOf(", department"));
                                    contentsStr = contentsStr.substring(0, contentsStr.indexOf(", suggestionCount="));

                                    if(intent.getStringExtra("majorStr").contains(departStr) &&
                                            intent.getStringExtra("professorStr").contains(proStr) &&
                                            intent.getStringExtra("lectureStr").contains(lecStr) ) {
                                        lecturePostArray.add(new LecturePostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr,
                                                departStr, proStr, lecStr));
                                    }

                                }
                            }
                        }

                        if (lecturePostArray.size() > 0) {
                            //Comparator ??? ?????????.
                            Comparator<LecturePostDTO> myComparator = new Comparator<LecturePostDTO>() {
                                @Override
                                public int compare(LecturePostDTO post1, LecturePostDTO post2) {
                                    return post2.date.compareTo(post1.date);
                                }
                            };

                            Collections.sort(lecturePostArray, myComparator);

                            for (int i = 0; i < lecturePostArray.size(); i++)
                                ladapter.addItem(lecturePostArray.get(i));

                            listView.setAdapter(ladapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "?????? ????????? ????????????.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LectureList.class);
                            startActivity(i);

                            finish();
                        }
                    }
                }
            });
        } else {
            mDatabase.child("Post").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "??????",
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
                                        if (!titleStr.substring(titleStr.indexOf("=") + 1, titleStr.indexOf("=") + 2).equals("???")) {
                                            commStr = "?????? " + titleStr.substring(titleStr.indexOf("=") + 1, titleStr.indexOf("=") + 2);
                                        } else {
                                            commStr = "?????? " + titleStr.substring(titleStr.indexOf("???") + 2, titleStr.indexOf("???") + 3);
                                        }
                                        titleStr = titleStr.substring(0, titleStr.indexOf(", commentCount="));
                                    } else {
                                        commStr = "?????? 0";
                                    }
                                    if (contentsStr.contains(", suggestionCount=")) {
                                        if (!contentsStr.substring(contentsStr.indexOf("=") + 1, contentsStr.indexOf("=") + 2).equals("???")) {
                                            suggStr = "?????? " + contentsStr.substring(contentsStr.indexOf("=") + 1, contentsStr.indexOf("=") + 2);
                                        } else {
                                            suggStr = "?????? " + contentsStr.substring(contentsStr.indexOf("???") + 2, contentsStr.indexOf("???") + 3);
                                        }
                                        contentsStr = contentsStr.substring(0, contentsStr.indexOf(", suggestionCount="));
                                    } else {
                                        suggStr = "?????? 0";
                                    }
                                    if (uidStr.contains("{")) {
                                        uidStr = " " + uidStr.substring(uidStr.indexOf("{") + 1, uidStr.length());
                                    }
                                    if (uidStr.contains(",")) {
                                        uidStr = uidStr.substring(uidStr.indexOf(",") + 1, uidStr.length());
                                    }

                                    if (intent.getStringExtra("text").contains(titleStr)) {
                                        postArray.add(new PostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr));
                                    }
                                }
                            }
                        }

                        if (postArray.size() > 0) {
                            //Comparator ??? ?????????.
                            Comparator<PostDTO> myComparator = new Comparator<PostDTO>() {
                                @Override
                                public int compare(PostDTO post1, PostDTO post2) {
                                    return post2.date.compareTo(post1.date);
                                }
                            };

                            Collections.sort(postArray, myComparator);

                            for (int i = 0; i < postArray.size(); i++)
                                adapter.addItem(postArray.get(i));

                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "?????? ????????? ????????????.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), BoardList.class);
                            startActivity(i);

                            finish();
                        }
                    }
                }
            });
        }

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

        // Adapter??? ????????? ???????????? ???????????? ?????? ArrayList
        private ArrayList<PostDTO> listViewItemList = new ArrayList<PostDTO>() ;

        // ListViewAdapter??? ?????????
        public BoardListViewAdapter() {

        }

        // Adapter??? ???????????? ???????????? ????????? ??????. : ?????? ??????
        @Override
        public int getCount() {
            return listViewItemList.size() ;
        }

        // position : ?????? ??? ?????? ?????? ??????
        // convertView : ????????? ?????? ??? position??? ???????????? ?????? ??? ( if == null ????????? ?????? )
        // parent : ????????? ?????? ???, ????????? ???
        // position??? ????????? ???????????? ????????? ??????????????? ????????? View??? ??????. : ?????? ??????
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

        // ????????? ??????(position)??? ?????? ???????????? ????????? ?????????(row)??? ID??? ??????. : ?????? ??????
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // ????????? ??????(position)??? ?????? ????????? ?????? : ?????? ??????
        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position) ;
        }

        // ????????? ????????? ????????? ?????? ??????.
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
    public class LectureListViewAdapter extends BaseAdapter {

        private DatabaseReference mDatabase;

        // Adapter??? ????????? ???????????? ???????????? ?????? ArrayList
        private ArrayList<LecturePostDTO> listViewItemList = new ArrayList<LecturePostDTO>() ;

        // ListViewAdapter??? ?????????
        public LectureListViewAdapter() {

        }

        // Adapter??? ???????????? ???????????? ????????? ??????. : ?????? ??????
        @Override
        public int getCount() {
            return listViewItemList.size() ;
        }

        // position : ?????? ??? ?????? ?????? ??????
        // convertView : ????????? ?????? ??? position??? ???????????? ?????? ??? ( if == null ????????? ?????? )
        // parent : ????????? ?????? ???, ????????? ???
        // position??? ????????? ???????????? ????????? ??????????????? ????????? View??? ??????. : ?????? ??????
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BoardListViewItem listView = null;

            if (convertView == null) {
                listView = new BoardListViewItem(getApplicationContext());
            } else {
                listView = (BoardListViewItem)convertView;
            }
            LecturePostDTO post = listViewItemList.get(position);
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
                    intent.putExtra("depart", post.department);
                    intent.putExtra("pro", post.professor);
                    intent.putExtra("lec", post.lecture);

                    startActivity(intent);

                    finish();
                }
            });
            return listView;

        }

        // ????????? ??????(position)??? ?????? ???????????? ????????? ?????????(row)??? ID??? ??????. : ?????? ??????
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // ????????? ??????(position)??? ?????? ????????? ?????? : ?????? ??????
        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position) ;
        }

        // ????????? ????????? ????????? ?????? ??????.
        public void addItem(Drawable icon, String title, String desc) {
            //BoardListViewItem item = new BoardListViewItem();

        /*item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);*/



            //listViewItemList.add(item);
        }

        public void addItem(String title, String contents, String date) {
            LecturePostDTO post = new LecturePostDTO();
            post.contents = contents;
            post.title = title;
            post.date = date;
            listViewItemList.add(post);
        }

        public void addItem(LecturePostDTO item) {
            listViewItemList.add(item);
        }

        public void removeItem(Object item) {
            listViewItemList.remove(item);
        }
    }
}