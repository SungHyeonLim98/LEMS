package com.example.lems;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Post extends AppCompatActivity {

    ImageButton enroll, pushBtn;
    TextView title, date, contents, push, pushText, kind;
    View footer;
    EditText comment_edit;
    Button modify, delete;

    Comment_Adapter ca;
    ArrayList<Comment_Item> c_arr = new ArrayList<Comment_Item>();
    ListView comment_list;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    PostDTO post;
    LecturePostDTO lecPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        enroll = findViewById(R.id.Enroll);
        title = findViewById(R.id.Title);
        date = findViewById(R.id.Date);
        contents = findViewById(R.id.Contents);
        contents.setMovementMethod(new ScrollingMovementMethod());
        footer = getLayoutInflater().inflate(R.layout.footer, null, false);
        comment_list = (ListView) findViewById(R.id.comment_list);
        push = (TextView) findViewById(R.id.push);
        pushText = (TextView) findViewById(R.id.pushText);
        pushBtn = (ImageButton) findViewById(R.id.ddabong);
        modify = (Button) findViewById(R.id.modify);
        delete = (Button) findViewById(R.id.delete);
        kind = findViewById(R.id.kind);

        comment_list.addFooterView(footer);

        Intent intent = getIntent();
        post = new PostDTO(intent.getStringExtra("Uid"),
                intent.getStringExtra("title"),
                intent.getStringExtra("contents"),
                intent.getStringExtra("date"),
                intent.getStringExtra("comm"),
                intent.getStringExtra("sugg"));
        lecPost = new LecturePostDTO(intent.getStringExtra("Uid"),
                intent.getStringExtra("title"),
                intent.getStringExtra("contents"),
                intent.getStringExtra("date"),
                intent.getStringExtra("comm"),
                intent.getStringExtra("sugg"),
                intent.getStringExtra("depart"),
                intent.getStringExtra("pro"),
                intent.getStringExtra("lec"));

        if(post.Uid.contains("lecctuera")) {
            kind.setText(lecPost.department  + " ??? " + lecPost.professor + " ??? " + lecPost.lecture);
        } else {
            kind.setText("???????????????");
        }

        setList();
        setFooter();

        title.setText(intent.getStringExtra("title"));
        contents.setText(intent.getStringExtra("contents"));
        date.setText(intent.getStringExtra("date"));

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String tmp = "";

        // ?????? ????????????
        if(!post.Uid.contains("lecctuera")) {
            if(post.Uid.contains("},")) {
                post.Uid = post.Uid.substring(post.Uid.indexOf("},")+2);
            }
            mDatabase.child("Comments").child(post.Uid.substring(1)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "??????",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String values = String.valueOf(task.getResult().getValue());
                        String[] result = values.split("contents=|date=");

                        push.setText(String.valueOf((result.length - 1) / 2));
                        String contentsStr, dateStr;

                        for (int i = 1; i < result.length - 1; i += 2) {
                            dateStr = result[i].substring(0, result[i].indexOf(","));
                            contentsStr = result[i + 1].substring(0, result[i + 1].indexOf("postName=") - 2);

                            Comment_Item ci = new Comment_Item();
                            ci.setContent(contentsStr);
                            ci.setNickname("??????");
                            ci.setDate(dateStr);
                            c_arr.add(ci);
                        }

                        //Comparator ??? ?????????.
                        Comparator<PostDTO> myComparator = new Comparator<PostDTO>() {
                            @Override
                            public int compare(PostDTO post1, PostDTO post2) {
                                return post2.date.compareTo(post1.date);
                            }
                        };
                    }
                }
            });
        } else {
            mDatabase.child("Comments").child(post.Uid.substring(post.Uid.indexOf("},")+3)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "??????",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String values = String.valueOf(task.getResult().getValue());
                        String[] result = values.split("contents=|date=");

                        push.setText(String.valueOf((result.length - 1) / 2));
                        String contentsStr, dateStr;

                        for (int i = 1; i < result.length - 1; i += 2) {
                            dateStr = result[i].substring(0, result[i].indexOf(","));
                            contentsStr = result[i + 1].substring(0, result[i + 1].indexOf("postName=") - 2);

                            Comment_Item ci = new Comment_Item();
                            ci.setContent(contentsStr);
                            ci.setNickname("??????");
                            ci.setDate(dateStr);
                            c_arr.add(ci);
                        }

                        //Comparator ??? ?????????.
                        Comparator<PostDTO> myComparator = new Comparator<PostDTO>() {
                            @Override
                            public int compare(PostDTO post1, PostDTO post2) {
                                return post2.date.compareTo(post1.date);
                            }
                        };
                    }
                }
            });
        }


        // ?????? ????????????
        mDatabase.child("suggestion").child(post.Uid.substring(1)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "??????",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String values = String.valueOf(task.getResult().getValue());
                    if(values.contains("check")) {
                        String[] result = values.split("=check");
                        pushText.setText(String.valueOf(result.length-1));
                    }
                }
            }
        });

        delete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                String postUid = post.Uid.substring(1, post.Uid.indexOf("2021"));
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(!postUid.equals(user.getUid())) {
                    Toast.makeText(getApplicationContext(), "?????? ????????? ????????????.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);

                builder.setTitle("??????").setMessage("?????? ???????????? ?????????????????????????");
                builder.setPositiveButton("?????????", null);
                builder.setNegativeButton("???", null);

                builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase.child("Post").child(post.Uid.substring(1, post.Uid.length())).setValue(null);

                        Intent intent = new Intent(getApplicationContext(), BoardList.class);

                        startActivity(intent);

                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();

                alertDialog.show();
                return false;
            }
        });

        modify.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(post.Uid.contains("lecctuera")) {
                    String postUid = post.Uid.substring(1, post.Uid.indexOf("2021"));
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(!postUid.equals(user.getUid())) {
                        Toast.makeText(getApplicationContext(), "?????? ????????? ????????????.",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    String postUid = post.Uid.substring(1, post.Uid.indexOf("2021"));
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(!postUid.equals(user.getUid())) {
                        Toast.makeText(getApplicationContext(), "?????? ????????? ????????????.",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }

                if(post.Uid.contains("lecctuera")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);

                    builder.setTitle("??????").setMessage("?????????????????????????");
                    builder.setPositiveButton("?????????", null);
                    builder.setNegativeButton("???", null);

                    builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ModifyBoard.class);
                            intent.putExtra("title", title.getText().toString());
                            intent.putExtra("contents", contents.getText().toString());
                            intent.putExtra("postUid", lecPost.Uid);
                            intent.putExtra("postDate", lecPost.date);
                            intent.putExtra("postDate", lecPost.lecture);
                            intent.putExtra("postDate", lecPost.professor);
                            intent.putExtra("postDate", lecPost.department);
                            startActivity(intent);

                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Post.this);

                    builder.setTitle("??????").setMessage("?????????????????????????");
                    builder.setPositiveButton("?????????", null);
                    builder.setNegativeButton("???", null);

                    builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ModifyBoard.class);
                            intent.putExtra("title", title.getText().toString());
                            intent.putExtra("contents", contents.getText().toString());
                            intent.putExtra("postUid", post.Uid);
                            intent.putExtra("postDate", post.date);
                            startActivity(intent);

                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                }

                return false;
            }
        });

        pushBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                mDatabase.child("suggestion").child(post.Uid.substring(1)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "??????",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if(String.valueOf(task.getResult().getValue()).contains(user.getUid())) {
                                Toast.makeText(getApplicationContext(), "?????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                            } else {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                String pu = post.Uid.substring(1);

                                post.suggestionCount = String.valueOf(Integer.valueOf(pushText.getText().toString()));
                                mDatabase.child("Post").child(pu).setValue(post);

                                mDatabase.child("suggestion").child(post.Uid.substring(1)).child(user.getUid()).setValue("check");

                                pushText.setText(String.valueOf(Integer.valueOf(pushText.getText().toString()) + 1));
                            }
                        }
                    }
                });

                return false;
            }
        });

        enroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!post.Uid.contains("lecctuera")) {
                    Intent intent = new Intent(getApplicationContext(), BoardList.class);
                    startActivity(intent);

                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LectureList.class);
                    startActivity(intent);

                    finish();
                }
                return false;
            }
        });

    }

    private void setFooter() {
        //Footer??? ??????????????? ?????? footer.??? ????????????!
        comment_edit = (EditText) footer.findViewById(R.id.jrv_comment_edit);
        ImageButton commentinput_btn = (ImageButton) footer.findViewById(R.id.jrv_commentinput_btn);
        //implements??? ????????? ????????? ?????? ?????????, setOnClickListener??? ????????? ????????? ??? ????????????.
        commentinput_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.jrv_commentinput_btn:
                        String temp = comment_edit.getText().toString();
                        if (temp.equals("")) {
                            Toast.makeText(getApplicationContext(), "????????? ????????????.", Toast.LENGTH_SHORT).show();
                        } else {
                            //EditText??? ????????? ?????? ?????? ??????!
                            Comment_Item ci = new Comment_Item();
                            ci.setContent(temp);
                            ci.setNickname("??????");
                            c_arr.add(ci);
                            resetAdapter();
                            comment_edit.setText("");

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            CommentDTO commentDTO = new CommentDTO();
                            long now = System.currentTimeMillis();
                            Date date = new Date(now);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                            if(post.Uid.contains("lecctuera")) {
                                commentDTO.postName = lecPost.title;
                                commentDTO.userUid = user.getUid();
                                commentDTO.contents = temp;
                                commentDTO.date = dateFormat.format(date);
                                String pu = post.Uid.substring(1);

                                if(pu.contains("},")) {
                                    pu = pu.substring(pu.indexOf("},")+3);
                                }
                                mDatabase.child("Comments").child(pu).child(commentDTO.userUid
                                        + commentDTO.date).setValue(commentDTO);

                                push.setText(String.valueOf(Integer.valueOf(push.getText().toString()) + 1));

                                lecPost.Uid = pu;
                                lecPost.commentCount = String.valueOf(Integer.valueOf(push.getText().toString()));
                                mDatabase.child("LecturePost").child(pu).setValue(lecPost);

                                FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                                mDatabase.child("UserCommentList").child(users.getUid()).child(pu).setValue("check");
                            } else {
                                commentDTO.postName = post.title;
                                commentDTO.userUid = user.getUid();
                                commentDTO.contents = temp;
                                commentDTO.date = dateFormat.format(date);
                                String pu = post.Uid.substring(1);

                                if(pu.contains("=")) {
                                    pu = pu.substring(pu.indexOf("=")+5);
                                }

                                mDatabase.child("Comments").child(pu).child(commentDTO.userUid
                                        + commentDTO.date).setValue(commentDTO);

                                push.setText(String.valueOf(Integer.valueOf(push.getText().toString()) + 1));

                                post.commentCount = String.valueOf(Integer.valueOf(push.getText().toString()));
                                mDatabase.child("Post").child(pu).setValue(post);

                                FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();

                                mDatabase.child("UserCommentList").child(users.getUid()).child(pu).setValue("check");
                            }

                        }
                        break;
                }
            }
        });
    }

    private void setList() {
        ca = new Comment_Adapter(getApplicationContext(), Post.this, null, c_arr);
        comment_list.setAdapter(ca);
        comment_list.setSelection(c_arr.size() - 1);
        comment_list.setDivider(null);
        comment_list.setSelectionFromTop(0, 0);
    }

    public void resetAdapter() {
        ca.notifyDataSetChanged();
        //Adapter??? ????????? ?????? ????????? ?????? ??? ???????????? ??????.
    }

    public void deleteArr(int p) {
        //Adapter?????? ??? ????????? ?????? ??? ???????????????. ???????????? ?????? ????????? id ?????? ???????????? c_arr ??? ???????????? ??????!
        c_arr.remove(p);
        //??????????????? ????????? ????????? ????????? Adapter ?????????(?)
        ca.notifyDataSetChanged();
    }
}