package com.example.lems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LectureList extends AppCompatActivity {

    Button searchBtn;
    ImageButton mypage, board, enroll;
    Spinner major, professor, lecture1;

    SearchView searchView;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_list);

        mypage = findViewById(R.id.mypage);
        board = findViewById(R.id.post);
        major = findViewById(R.id.major);
        professor = findViewById(R.id.professor);
        lecture1 = findViewById(R.id.lecture1);
        enroll = findViewById(R.id.Enroll);
        searchView = findViewById(R.id.searchView);
        ListView listView = findViewById(R.id.listView);
        searchBtn = findViewById(R.id.searchBtn);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String[] Major = {"??????","??????????????????????????????"};
        ArrayAdapter<String> madapter;
        madapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Major);
        major.setAdapter(madapter);

        final String[] Professor = {"??????","?????????","?????????","?????????","?????????","?????????","?????????","?????????","?????????",
                "?????????","?????????","?????????","?????????","?????????","?????????","?????????","?????????","??????"};
        ArrayAdapter<String> padapter;
        padapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Professor);
        professor.setAdapter(padapter);

        String[] Lecture1 = {"??????","JSP","JAVA","PHP","DBMS??????","C??????","C++","????????? ??????","?????? ????????????","????????????",
                "????????? ???????????????","???????????? ??????","??????????????????","??????????????????","??????????????????","??????????????????","?????????","????????????",
                "???????????????","??????????????? ????????? ?????????","??????????????? ????????? ??????","?????? ??????","????????? ?????? ??????","????????? ?????? ??????"};
        ArrayAdapter<String> ladapter;
        ladapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Lecture1);
        lecture1.setAdapter(ladapter);

        setListView();

        professor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (Professor[position]) {
                    case "?????????":
                        String[] newMajor={"??????????????????????????????"};
                        ArrayAdapter<String> madapter;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor);
                        major.setAdapter(madapter);
                        String[] newLecture = {"??????", "????????? ??????", "?????? ????????????", "????????????????????????"};
                        ArrayAdapter<String> ladapter1;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor1={"??????????????????????????????"};
                        ArrayAdapter<String> madapter1;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor1);
                        major.setAdapter(madapter);
                        String[] newLecture1 = {"??????","???????????? ??????"};
                        ArrayAdapter<String> ladpter1;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture1);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor2={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater2;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor2);
                        major.setAdapter(madapter);
                        String[] newLecture2 = {"??????","JSP"};
                        ArrayAdapter<String> mdapter2;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture2);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor3={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater3;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor3);
                        major.setAdapter(madapter);
                        String[] newLecture3 = {"??????","JAVA"};
                        ArrayAdapter<String> mdapter3;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture3);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor4={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater4;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor4);
                        major.setAdapter(madapter);
                        String[] newLecture4 = {"??????","??????????????????","??????????????????"};
                        ArrayAdapter<String> mdapter4;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture4);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor5={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater5;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor5);
                        major.setAdapter(madapter);
                        String[] newLecture5 = {"??????","??????????????????","???????????????","??????????????? ????????? ?????????","??????????????? ????????? ??????"};
                        ArrayAdapter<String> mdapter5;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture5);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor6={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater6;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor6);
                        major.setAdapter(madapter);
                        String[] newLecture6 = {"??????","??????????????????, ????????????"};
                        ArrayAdapter<String> mdapter6;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture6);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor7={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater7;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor7);
                        major.setAdapter(madapter);
                        String[] newLecture7 = {"??????","?????????"};
                        ArrayAdapter<String> mdapter7;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture7);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor8={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater8;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor8);
                        major.setAdapter(madapter);
                        String[] newLecture8 = {"??????","PHP","??????????????????"};
                        ArrayAdapter<String> mdapter8;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture8);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor9={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater9;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor9);
                        major.setAdapter(madapter);
                        String[] newLecture9 = {"??????","??????????????????","????????????"};
                        ArrayAdapter<String> mdapter9;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture9);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor10={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater10;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor10);
                        major.setAdapter(madapter);
                        String[] newLecture10 = {"??????","PHP"};
                        ArrayAdapter<String> mdapter10;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture10);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor11={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater11;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor11);
                        major.setAdapter(madapter);
                        String[] newLecture11 = {"??????","DBMS??????"};
                        ArrayAdapter<String> mdapter11;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture11);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor12={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater12;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor12);
                        major.setAdapter(madapter);
                        String[] newLecture12 = {"??????","C??????"};
                        ArrayAdapter<String> mdapter12;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture12);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor13={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater13;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor13);
                        major.setAdapter(madapter);
                        String[] newLecture13 = {"??????","C++"};
                        ArrayAdapter<String> mdapter13;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture13);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor14={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater14;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor14);
                        major.setAdapter(madapter);
                        String[] newLecture14 = {"??????","????????? ?????? ??????"};
                        ArrayAdapter<String> mdapter14;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture14);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "?????????":
                        String[] newMajor15={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater15;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor15);
                        major.setAdapter(madapter);
                        String[] newLecture15 = {"??????","????????? ?????? ??????"};
                        ArrayAdapter<String> mdapter15;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture15);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "??????":
                        String[] newMajor16={"??????????????????????????????"};
                        ArrayAdapter<String> mdapater16;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor16);
                        major.setAdapter(madapter);
                        String[] newLecture16 = {"??????","????????????", "??????????????????"};
                        ArrayAdapter<String> mdapter16;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture16);
                        lecture1.setAdapter(ladapter1);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String majorStr = major.getSelectedItem().toString();
                String professorStr = professor.getSelectedItem().toString();
                String lectureStr = lecture1.getSelectedItem().toString();

                Intent intent = new Intent(getApplicationContext(), ListSearchView.class);
                intent.putExtra("lecture", "lectureSpinner");
                intent.putExtra("majorStr", majorStr);
                intent.putExtra("professorStr", professorStr);
                intent.putExtra("lectureStr", lectureStr);
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
                intent.putExtra("lecture", "lecture");
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

                FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                mDatabase.child("Users").child("user").child(users.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "??????",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String values = String.valueOf(task.getResult().getValue());
                            String[] result = values.split("gender=|major=|phone=|name=|" +
                                    "sign=|birth=|department=|email=");

                            String signStr;

                            signStr = result[5].substring(0, result[5].indexOf(","));

                            if(signStr.equals("????????????")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LectureList.this);

                                builder.setTitle("").setMessage("?????? ????????? ????????????. ?????? ????????? ??????????????????.");
                                builder.setPositiveButton("??????", null);

                                AlertDialog alertDialog = builder.create();

                                alertDialog.show();
                            } else {
                                Intent intent = new Intent(getApplicationContext(), Lecture.class);
                                startActivity(intent);

                                finish();
                            }
                        }
                    }
                });

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

        board.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), BoardList.class);
                startActivity(intent);

                finish();
                return false;
            }
        });
    }

    public void setListView() {
        LectureList.BoardListViewAdapter adapter = new BoardListViewAdapter();
        ArrayList<LecturePostDTO> postArray = new ArrayList<LecturePostDTO>();

        ListView listView = findViewById(R.id.listView);
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
                                postArray.add(new LecturePostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr,
                                        departStr, proStr, lecStr));
                            }
                        }
                    }

                    //Comparator ??? ?????????.
                    Comparator<LecturePostDTO> myComparator = new Comparator<LecturePostDTO>() {
                        @Override
                        public int compare(LecturePostDTO post1, LecturePostDTO post2) {
                            return post2.date.compareTo(post1.date);
                        }
                    };

                    Collections.sort(postArray, myComparator);

                    for (int i = 0; i < postArray.size(); i++)
                        adapter.addItem(postArray.get(i));

                    listView.setAdapter(adapter);
                }
            }
        });
    }

    public class BoardListViewAdapter extends BaseAdapter {

        private DatabaseReference mDatabase;

        // Adapter??? ????????? ???????????? ???????????? ?????? ArrayList
        private ArrayList<LecturePostDTO> listViewItemList = new ArrayList<LecturePostDTO>() ;

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