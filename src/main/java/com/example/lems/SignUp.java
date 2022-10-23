package com.example.lems;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class SignUp extends AppCompatActivity {

    EditText pwd1, pwd2, name, id, email, birth, fn, phone1, phone2, department, major;
    Button signUpBtn, overlapBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private boolean overlap = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = (EditText) findViewById(R.id.Email);
        name = (EditText) findViewById(R.id.Name);
        pwd1 = (EditText) findViewById(R.id.Password);
        pwd2 = (EditText) findViewById(R.id.Password2);
        birth = (EditText) findViewById(R.id.Birth);
        fn = (EditText) findViewById(R.id.FirstNum);
        phone1 = (EditText) findViewById(R.id.PhoneNum2);
        phone2 = (EditText) findViewById(R.id.PhoneNum3);
        department = findViewById(R.id.department);
        major = findViewById(R.id.major);

        signUpBtn = (Button) findViewById(R.id.SignUp);
        overlapBtn = (Button) findViewById(R.id.OverLap);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        overlapBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mDatabase.child("Users").child("user").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "실패",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(String.valueOf(task.getResult().getValue()).contains(email.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "중복된 이메일",
                                        Toast.LENGTH_SHORT).show();
                                overlap = false;
                            } else {
                                Toast.makeText(getApplicationContext(), "사용 가능",
                                        Toast.LENGTH_SHORT).show();
                                overlap = true;
                            }
                        }
                    }
                });

               return false;

            }
        });

        signUpBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!overlap) {
                    Toast.makeText(getApplicationContext(), "이메일 중복확인을 하십시오.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(!pwd1.getText().toString().equals(pwd2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(name.getText().toString().equals("") || birth.getText().toString().equals("") ||
                        fn.getText().toString().equals("") || phone1.getText().toString().equals("") ||
                        phone2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "빈칸을 확인하십시오.",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

                createUser(email.getText().toString(), pwd2.getText().toString());



                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);

                builder.setTitle("").setMessage("회원가입 성공");
                builder.setNegativeButton("확인", null);

                builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String userUid = getUserUid();
                        String genderStr, nameStr, emailStr, phoneStr, birthStr, departmentStr, majorStr;

                        nameStr = name.getText().toString();
                        emailStr = email.getText().toString();
                        phoneStr = "010" + phone1.getText().toString() + phone2.getText().toString();
                        birthStr = birth.getText().toString();
                        departmentStr = department.getText().toString();
                        majorStr = major.getText().toString();

                        if(Integer.valueOf(fn.getText().toString()) % 2 == 1)
                            genderStr = "남";
                        else
                            genderStr = "여";

                        UserDTO userDTO = new UserDTO(nameStr, emailStr, phoneStr, birthStr, genderStr, departmentStr, majorStr);

                        mDatabase.child("Users").child("user").child(userUid).setValue(userDTO);

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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "회원가입 성공",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else {
                        }
                    }
                });
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

    private void getDatabaseEmail(String email) {
        mDatabase.child("Users").child("user").child(email).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                    Toast.makeText(getApplicationContext(), "이메일 없음",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    String result =  String.valueOf(task.getResult().getValue());

                    Toast.makeText(getApplicationContext(), "이메일 있음",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getUserUid() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }
}