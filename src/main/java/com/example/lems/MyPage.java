package com.example.lems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyPage extends AppCompatActivity {

    Button modifyBtn, logout, myCommBtn;
    ImageView profile, lecture, board;
    TextView name, major, sign;

    private FirebaseStorage storage;
    private int GALLEY_CODE = 10;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        lecture = findViewById(R.id.lecture);
        board = findViewById(R.id.post);
        modifyBtn = findViewById(R.id.ProfileModify);
        profile = findViewById(R.id.Profile);
        logout = findViewById(R.id.logout);
        myCommBtn = findViewById(R.id.commentBtn);
        name = findViewById(R.id.Name);
        major = findViewById(R.id.Major);
        sign = findViewById(R.id.Univers);

        ListView myFreePostList = findViewById(R.id.myFreePostList);
        ListView myLecturePostList = findViewById(R.id.myLecturePostList);
        //ListView myCommentList = findViewById(R.id.myCommentList);
        //ListView myLectureCommentList = findViewById(R.id.myLectureCommentList);
        MyPage.BoardListViewAdapter adapter = new MyPage.BoardListViewAdapter();
        MyPage.LectureListViewAdapter ladapter = new MyPage.LectureListViewAdapter();
        MyPage.BoardListViewAdapter cadapter = new MyPage.BoardListViewAdapter();
        MyPage.LectureListViewAdapter lcadapter = new MyPage.LectureListViewAdapter();
        storage = FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();

        ArrayList<PostDTO> postArray = new ArrayList<PostDTO>();

        // 내 게시물 자유게시판
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

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if(uidStr.contains(user.getUid())) {
                                    postArray.add(new PostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr));
                                }
                            }
                        }
                    }

                    //Comparator 를 만든다.
                    Comparator<PostDTO> myComparator = new Comparator<PostDTO>() {
                        @Override
                        public int compare(PostDTO post1, PostDTO post2) {
                            return post2.date.compareTo(post1.date);
                        }
                    };

                    Collections.sort(postArray, myComparator);

                    for (int i = 0; i < postArray.size(); i++)
                        adapter.addItem(postArray.get(i));

                    myFreePostList.setAdapter(adapter);
                }
            }
        });

        ArrayList<LecturePostDTO> lecturePostArray = new ArrayList<LecturePostDTO>();

        // 내 게시물 강의게시판
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

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                if(uidStr.contains(user.getUid())) {
                                    lecturePostArray.add(new LecturePostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr,
                                            departStr, proStr, lecStr));
                                }
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

                    Collections.sort(lecturePostArray, myComparator);

                    for (int i = 0; i < lecturePostArray.size(); i++)
                        ladapter.addItem(lecturePostArray.get(i));

                    myLecturePostList.setAdapter(ladapter);
                }
            }
        });

        //회원 정보 불러오기
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
                    String s = "";
                    for(int i=0; i<result.length-1; i++)
                        s += "result[" + String.valueOf(i) + "]=" + result[i] + "\n";
                    String genderStr, departmentStr, phoneStr, nameStr, signStr, numStr, majorStr;

                    genderStr = result[1].substring(0, result[1].length()-2);
                    departmentStr = result[2].substring(0, result[2].indexOf(","));
                    phoneStr = result[3].substring(0, result[1].length()-2);
                    nameStr = result[4].substring(0, result[4].indexOf(","));
                    signStr = result[5].substring(0, result[5].indexOf(","));
                    numStr = result[6].substring(0, result[1].length()-2);
                    majorStr = result[7].substring(0, result[1].length()-2);

                    name.setText("이름: " + nameStr);
                    major.setText("전공: " + departmentStr);
                    sign.setText("인증 상태: " + signStr);
                }
            }
        });

        myCommBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), MyPageComm.class);
                startActivity(intent);

                finish();
                return false;
            }
        });

        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();

                return false;
            }
        });

        modifyBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLEY_CODE);

                //downloadImg();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLEY_CODE) {
            try { // Create a storage reference from our app
                StorageReference storageRef = storage.getReference();
                Uri file = data.getData();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final StorageReference riversRef = storageRef.child("images/" + user.getUid() + "profile");
                UploadTask uploadTask = riversRef.putFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        CursorLoader cursorLoader = new CursorLoader(getApplicationContext(), uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

    /* private  void downloadImg() {
        StorageReference storageRef = storage.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef.child("images/" + user.getUid() + "profile").getDownloadUrl().addOnSuccessListener
        (new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getApplicationContext())
                        .load(uri)
                        .into(profile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }*/
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

    public class LectureListViewAdapter extends BaseAdapter {

        private DatabaseReference mDatabase;

        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<LecturePostDTO> listViewItemList = new ArrayList<LecturePostDTO>() ;

        // ListViewAdapter의 생성자
        public LectureListViewAdapter() {

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