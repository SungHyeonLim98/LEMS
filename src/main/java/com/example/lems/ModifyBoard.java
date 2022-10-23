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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModifyBoard extends AppCompatActivity {

    public static final int PICK_FROM_ALBUM = 1;
    private Uri imageUri;
    private String pathUri;
    private File tempFile;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;

    ImageButton exit, enroll;
    EditText title, contents;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        exit = findViewById(R.id.Exit);
        enroll = findViewById(R.id.Enroll);
        title = findViewById(R.id.title);
        contents = findViewById(R.id.contents);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        contents.setText(intent.getStringExtra("contents"));

        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyBoard.this);

                builder.setTitle("취소").setMessage("게시글을 등록하지 않고 나가시겠습니까?");
                builder.setPositiveButton("아니요", null);
                builder.setNegativeButton("예", null);

                builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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

        enroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ModifyBoard.this);

                builder.setTitle("수정").setMessage("게시글을 수정하시겠습니까?");
                builder.setPositiveButton("아니요", null);
                builder.setNegativeButton("예", null);

                builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.setNegativeButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        PostDTO postDTO = new PostDTO(user.getUid(), title.getText().toString(),
                                contents.getText().toString(), intent.getStringExtra("postDate"));

                        String postUid = intent.getStringExtra("postUid").substring(1);

                        mDatabase.child("Post").child(postUid).setValue(postDTO);

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
    }
}