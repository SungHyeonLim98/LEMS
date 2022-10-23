package com.example.lems;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Lecture extends AppCompatActivity {

    Spinner major, professor, lecture1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;

    ImageButton exit, enroll;
    EditText title, contents;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);

        exit = findViewById(R.id.Exit);
        enroll = findViewById(R.id.Enroll);
        title = findViewById(R.id.title);
        contents = findViewById(R.id.contents);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        major = findViewById(R.id.major);
        professor = findViewById(R.id.professor);
        lecture1 = findViewById(R.id.lecture1);

        final String[] Major = {"학과","컴퓨터소프트웨어학과"};
        ArrayAdapter<String> madapter;
        madapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Major);
        major.setAdapter(madapter);

        final String[] Professor = {"교수","서장원","노상수","오민근","염세훈","안현섭","문혜경","김두상","류문형",
                "조민양","유상호","김판용","서동린","임경철","조용성","오경란","장승익","초염"};
        ArrayAdapter<String> padapter;
        padapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Professor);
        professor.setAdapter(padapter);

        final String[] Lecture1 = {"강의","JSP","JAVA","PHP","DBMS실습","C언어","C++","컴퓨터 통신","창업 아카데미","졸업작품",
                "모바일 프로그래밍","빅데이터 분석","인공지능개론","객체지향설계","데이터베이스","자바스크립트","파이썬","알고리즘",
                "취업멘토링","스마트워크 캡스톤 디자인","스마트워크 맞춤형 실무","채널 운영","유튜브 영상 제작","유튜브 영상 기획"};
        ArrayAdapter<String> ladapter;
        ladapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Lecture1);
        lecture1.setAdapter(ladapter);

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


        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intent = new Intent(getApplicationContext(), LectureList.class);
                startActivity(intent);

                finish();

                return false;
            }
        });

        enroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(major.getSelectedItem().toString().equals("학과")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Lecture.this);

                    builder.setTitle("").setMessage("학과를 선택하세요.");
                    builder.setPositiveButton("확인", null);

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                    return false;
                } else if (professor.getSelectedItem().toString().equals("교수")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Lecture.this);

                    builder.setTitle("").setMessage("교수명을 선택하세요.");
                    builder.setPositiveButton("확인", null);

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                    return false;
                } else if (lecture1.getSelectedItem().toString().equals("강의")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Lecture.this);

                    builder.setTitle("").setMessage("강의를 선택하세요.");
                    builder.setPositiveButton("확인", null);

                    AlertDialog alertDialog = builder.create();

                    alertDialog.show();
                    return false;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                LecturePostDTO postDTO = new LecturePostDTO();
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                UserDTO userDTO = new UserDTO();

                postDTO.Uid = user.getUid();
                postDTO.title = title.getText().toString();
                postDTO.contents = contents.getText().toString();
                postDTO.date = dateFormat.format(date);
                postDTO.department = major.getSelectedItem().toString();
                postDTO.professor = professor.getSelectedItem().toString();
                postDTO.lecture = lecture1.getSelectedItem().toString();
                postDTO.commentCount = "0";
                postDTO.suggestionCount = "0";

                mDatabase.child("LecturePost").child(postDTO.Uid+postDTO.date+"lecctuera").setValue(postDTO);

                Toast.makeText(getApplicationContext(), "등록 되었습니다.",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), LectureList.class);
                startActivity(intent);

                finish();
                return false;
            }
        });
    }
}