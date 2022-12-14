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

public class MyPageComm extends AppCompatActivity {

    Button modifyBtn, logout, myPostBtn;
    ImageView profile, lecture, board;
    TextView name, major, sign;

    private FirebaseStorage storage;
    private int GALLEY_CODE = 10;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabase2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_comm);

        lecture = findViewById(R.id.lecture);
        board = findViewById(R.id.post);
        modifyBtn = findViewById(R.id.ProfileModify);
        profile = findViewById(R.id.Profile);
        logout = findViewById(R.id.logout);
        myPostBtn = findViewById(R.id.boardBtn);
        name = findViewById(R.id.Name);
        major = findViewById(R.id.Major);
        sign = findViewById(R.id.Univers);

        ListView myFreePostList = findViewById(R.id.myFreeCommentList);
        ListView myLecturePostList = findViewById(R.id.myLectureCommentList);
        //ListView myCommentList = findViewById(R.id.myCommentList);
        //ListView myLectureCommentList = findViewById(R.id.myLectureCommentList);
        MyPageComm.BoardListViewAdapter adapter = new MyPageComm.BoardListViewAdapter();
        MyPageComm.LectureListViewAdapter ladapter = new MyPageComm.LectureListViewAdapter();
        MyPageComm.BoardListViewAdapter cadapter = new MyPageComm.BoardListViewAdapter();
        MyPageComm.LectureListViewAdapter lcadapter = new MyPageComm.LectureListViewAdapter();
        storage = FirebaseStorage.getInstance();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase2 = FirebaseDatabase.getInstance().getReference();

        ArrayList<PostDTO> postArray = new ArrayList<PostDTO>();

        ArrayList<PostDTO> myCommPostArray = new ArrayList<PostDTO>();
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();

        // ?????? ?????? ??? ????????? ???????????????
        mDatabase.child("UserCommentList").child(users.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "??????",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String values = String.valueOf(task.getResult().getValue());
                    String[] commResult = values.split("check");

                    mDatabase2.child("Post").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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


                                    myCommPostArray.add(new PostDTO(titleStr, contentsStr, dateStr));

                                } else {
                                    String w = "";
                                    for (int i = 0; i < result.length - 1; i += 4) {
                                        if (i + 5 >= result.length)
                                            titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("}}"));
                                        else {
                                            titleStr = result[i + 4].substring(0, result[i + 4].lastIndexOf("},"));
                                            contentsStr = result[i + 3].substring(0, result[i + 3].length() - 2);
                                            dateStr = result[i + 1].substring(0, result[i + 1].length() - 2);
                                            uidStr = result[i].substring(0, result[i].length() - 2);

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
                                            if (uidStr.contains("},")) {
                                                uidStr = uidStr.substring(uidStr.indexOf("},") + 2, uidStr.length());
                                            }
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            for(int j=0; j<commResult.length-1; j++){
                                                if(commResult[j].substring(1).contains(uidStr)) {
                                                    myCommPostArray.add(new PostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr));
                                                }
                                            }
                                        }
                                    }
                                }

                                //Comparator ??? ?????????.
                                Comparator<PostDTO> myComparator = new Comparator<PostDTO>() {
                                    @Override
                                    public int compare(PostDTO post1, PostDTO post2) {
                                        return post2.date.compareTo(post1.date);
                                    }
                                };

                                Collections.sort(myCommPostArray, myComparator);

                                for (int i = 0; i < myCommPostArray.size(); i++)
                                    cadapter.addItem(myCommPostArray.get(i));

                                myFreePostList.setAdapter(cadapter);
                            }
                        }
                    });
                }
            }
        });

        ArrayList<LecturePostDTO> myLectureCommPostArray = new ArrayList<LecturePostDTO>();

        // ?????? ?????? ??? ????????? ???????????????
        mDatabase.child("UserCommentList").child(users.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "??????",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String values = String.valueOf(task.getResult().getValue());
                    String[] commResult = values.split("check");

                    mDatabase2.child("LecturePost").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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


                                    //myLectureCommPostArray.add(new PostDTO(titleStr, contentsStr, dateStr));

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
                                            if (uidStr.contains("},")) {
                                                uidStr = uidStr.substring(uidStr.indexOf("},") + 1, uidStr.length());
                                            }

                                            departStr = contentsStr.substring(contentsStr.indexOf("department=")+11);
                                            lecStr = contentsStr.substring(contentsStr.indexOf("lecture=")+8, contentsStr.indexOf(", department"));
                                            contentsStr = contentsStr.substring(0, contentsStr.indexOf(", suggestionCount="));

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                            for(int j=0; j<commResult.length-1; j++){
                                                if(commResult[j].contains(uidStr)) {
                                                    myLectureCommPostArray.add(new LecturePostDTO(uidStr, titleStr, contentsStr, dateStr, commStr, suggStr,
                                                            departStr, proStr, lecStr));
                                                }
                                            }
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

                                Collections.sort(myLectureCommPostArray, myComparator);

                                for (int i = 0; i < myLectureCommPostArray.size(); i++)
                                    lcadapter.addItem(myLectureCommPostArray.get(i));

                                myLecturePostList.setAdapter(lcadapter);
                            }
                        }
                    });
                }
            }
        });

        //?????? ?????? ????????????
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.child("Users").child("user").child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "??????",
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

                    name.setText("??????: " + nameStr);
                    major.setText("??????: " + departmentStr);
                    sign.setText("?????? ??????: " + signStr);
                }
            }
        });

        myPostBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), MyPage.class);
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