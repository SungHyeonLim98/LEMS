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

        final String[] Major = {"학과","컴퓨터소프트웨어학과"};
        ArrayAdapter<String> madapter;
        madapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Major);
        major.setAdapter(madapter);

        final String[] Professor = {"교수","서장원","노상수","오민근","염세훈","안현섭","문혜경","김두상","류문형",
                "조민양","유상호","김판용","서동린","임경철","조용성","오경란","장승익","초염"};
        ArrayAdapter<String> padapter;
        padapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Professor);
        professor.setAdapter(padapter);

        String[] Lecture1 = {"강의","JSP","JAVA","PHP","DBMS실습","C언어","C++","컴퓨터 통신","창업 아카데미","졸업작품",
                "모바일 프로그래밍","빅데이터 분석","인공지능개론","객체지향설계","데이터베이스","자바스크립트","파이썬","알고리즘",
                "취업멘토링","스마트워크 캡스톤 디자인","스마트워크 맞춤형 실무","채널 운영","유튜브 영상 제작","유튜브 영상 기획"};
        ArrayAdapter<String> ladapter;
        ladapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Lecture1);
        lecture1.setAdapter(ladapter);

        setListView();

        professor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (Professor[position]) {
                    case "서장원":
                        String[] newMajor={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> madapter;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor);
                        major.setAdapter(madapter);
                        String[] newLecture = {"강의", "컴퓨터 통신", "창업 아카데미", "졸업작품종합설계"};
                        ArrayAdapter<String> ladapter1;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "노상수":
                        String[] newMajor1={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> madapter1;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor1);
                        major.setAdapter(madapter);
                        String[] newLecture1 = {"강의","빅데이터 분석"};
                        ArrayAdapter<String> ladpter1;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture1);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "오민근":
                        String[] newMajor2={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater2;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor2);
                        major.setAdapter(madapter);
                        String[] newLecture2 = {"강의","JSP"};
                        ArrayAdapter<String> mdapter2;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture2);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "염세훈":
                        String[] newMajor3={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater3;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor3);
                        major.setAdapter(madapter);
                        String[] newLecture3 = {"강의","JAVA"};
                        ArrayAdapter<String> mdapter3;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture3);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "안현섭":
                        String[] newMajor4={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater4;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor4);
                        major.setAdapter(madapter);
                        String[] newLecture4 = {"강의","인공지능개론","객체지향설계"};
                        ArrayAdapter<String> mdapter4;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture4);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "문혜경":
                        String[] newMajor5={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater5;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor5);
                        major.setAdapter(madapter);
                        String[] newLecture5 = {"강의","데이터베이스","취업멘토링","스마트워크 캡스톤 디자인","스마트워크 맞춤형 실무"};
                        ArrayAdapter<String> mdapter5;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture5);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "김두상":
                        String[] newMajor6={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater6;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor6);
                        major.setAdapter(madapter);
                        String[] newLecture6 = {"강의","자바스크립트, 채널운영"};
                        ArrayAdapter<String> mdapter6;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture6);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "류문형":
                        String[] newMajor7={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater7;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor7);
                        major.setAdapter(madapter);
                        String[] newLecture7 = {"강의","파이썬"};
                        ArrayAdapter<String> mdapter7;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture7);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "조민양":
                        String[] newMajor8={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater8;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor8);
                        major.setAdapter(madapter);
                        String[] newLecture8 = {"강의","PHP","졸업작품설계"};
                        ArrayAdapter<String> mdapter8;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture8);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "유상호":
                        String[] newMajor9={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater9;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor9);
                        major.setAdapter(madapter);
                        String[] newLecture9 = {"강의","빅데이터분석","알고리즘"};
                        ArrayAdapter<String> mdapter9;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture9);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "김판용":
                        String[] newMajor10={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater10;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor10);
                        major.setAdapter(madapter);
                        String[] newLecture10 = {"강의","PHP"};
                        ArrayAdapter<String> mdapter10;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture10);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "서동린":
                        String[] newMajor11={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater11;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor11);
                        major.setAdapter(madapter);
                        String[] newLecture11 = {"강의","DBMS실습"};
                        ArrayAdapter<String> mdapter11;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture11);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "임경철":
                        String[] newMajor12={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater12;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor12);
                        major.setAdapter(madapter);
                        String[] newLecture12 = {"강의","C언어"};
                        ArrayAdapter<String> mdapter12;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture12);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "조용성":
                        String[] newMajor13={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater13;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor13);
                        major.setAdapter(madapter);
                        String[] newLecture13 = {"강의","C++"};
                        ArrayAdapter<String> mdapter13;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture13);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "오경란":
                        String[] newMajor14={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater14;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor14);
                        major.setAdapter(madapter);
                        String[] newLecture14 = {"강의","유튜브 영상 제작"};
                        ArrayAdapter<String> mdapter14;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture14);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "장승익":
                        String[] newMajor15={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater15;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor15);
                        major.setAdapter(madapter);
                        String[] newLecture15 = {"강의","유튜브 영상 기획"};
                        ArrayAdapter<String> mdapter15;
                        ladapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newLecture15);
                        lecture1.setAdapter(ladapter1);
                        break;

                    case "초염":
                        String[] newMajor16={"컴퓨터소프트웨어학과"};
                        ArrayAdapter<String> mdapater16;
                        madapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, newMajor16);
                        major.setAdapter(madapter);
                        String[] newLecture16 = {"강의","운영체제", "인공지능개론"};
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
                            Toast.makeText(getApplicationContext(), "실패",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String values = String.valueOf(task.getResult().getValue());
                            String[] result = values.split("gender=|major=|phone=|name=|" +
                                    "sign=|birth=|department=|email=");

                            String signStr;

                            signStr = result[5].substring(0, result[5].indexOf(","));

                            if(signStr.equals("인증필요")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LectureList.this);

                                builder.setTitle("").setMessage("해당 권한이 없습니다. 회원 인증을 완료해주세요.");
                                builder.setPositiveButton("확인", null);

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
                    Toast.makeText(getApplicationContext(), "실패",
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
                                } else {
                                    suggStr = "추천 0";
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

                    //Comparator 를 만든다.
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

        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<LecturePostDTO> listViewItemList = new ArrayList<LecturePostDTO>() ;

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