package com.example.lems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
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

public class Board extends AppCompatActivity {

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

        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), BoardList.class);
                startActivity(intent);

                finish();

                return false;
            }
        });

        enroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                PostDTO postDTO = new PostDTO();
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                UserDTO userDTO = new UserDTO();

                postDTO.Uid = user.getUid();
                postDTO.title = title.getText().toString();
                postDTO.contents = contents.getText().toString();
                postDTO.date = dateFormat.format(date);
                postDTO.suggestionCount = "0";
                postDTO.commentCount = "0";

                mDatabase.child("Post").child(postDTO.Uid+postDTO.date).setValue(postDTO);

                Toast.makeText(getApplicationContext(), "등록 되었습니다.",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), BoardList.class);
                startActivity(intent);

                finish();
                return false;
            }
        });
    }
}