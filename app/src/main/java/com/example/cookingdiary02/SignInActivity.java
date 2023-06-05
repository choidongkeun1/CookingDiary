package com.example.cookingdiary02;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {
    TextInputLayout layoutEmail, layoutPw;
    String email, pw;
    String dbEm = "";
    String dbPw = "";
    DBHelper dbHelper;
    SQLiteDatabase db;
    boolean pass = false;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        if (db == null) {
            db.execSQL("INSERT INTO INFO VALUES ('" + " " + "', '" + " " + "', '" + " " + "');");
        }

        // Text 입력창 가리키는 변수
        layoutEmail = findViewById(R.id.layout_emailA);
        layoutPw = findViewById(R.id.layout_pwA);

        // 화면에 입력받은 값 가져오기
        EditText e = findViewById(R.id.login_email);
        EditText p = findViewById(R.id.login_pw);
        email = e.getText().toString();
        pw = p.getText().toString();

        // 로그인 버튼
        Button login = findViewById(R.id.login_button);
        // 입력하지 않은 초기 상태에 로그인 비활성화
        if (email.isEmpty() || pw.isEmpty()) {
            login.setClickable(false);
            login.setEnabled(false);
        }

        // 이메일 입력창 내용 변화 감지
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {
                // 이메일 조건 만족하지 않은 경우 오류
                if (s.toString().isEmpty() || !s.toString().matches("(.*)@(.*)+\\.(.*)")) {
                    layoutEmail.setError("이메일을 바르게 입력해주세요.");
                } else {
                    layoutEmail.setError(null);
                    // 다른 입력창 오류 시 로그인 비활성화 유지
                    if (p.getText().toString().isEmpty()) {
                        login.setClickable(false);
                        login.setEnabled(false);
                    } else {  // 로그인 활성화
                        login.setClickable(true);
                        login.setEnabled(true);
                    }
                }
            }
        });

        // 비밀번호 입력창 내용 변화 감지
        p.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable s) {
                // 비밀번호 입력이 빈 경우 오류
                if (s.toString().isEmpty()) {
                    layoutPw.setError("비밀번호를 입력해주세요");
                } else {
                    layoutPw.setError(null);
                    // 다른 입력창 오류 시 로그인 비활성화 유지
                    if (e.getText().toString().isEmpty() || !e.getText().toString().matches("(.*)@(.*)+\\.(.*)")) {
                        login.setClickable(false);
                        login.setEnabled(false);
                    } else {  // 로그인 활성화
                        login.setClickable(true);
                        login.setEnabled(true);
                    }
                }
            }
        });

        // 로그인 버튼 동작
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* 로그인 가능한지 확인하기 */
                // DB의 ID, PW값 읽기
                Cursor cursor = db.rawQuery("SELECT ID, PW FROM INFO", null);
                while (cursor.moveToNext()) {
                    dbEm = cursor.getString(0);
                    dbPw = cursor.getString(1);

                    // 입력정보와 데이터 비교
                    if (!(e.getText().toString().equals(dbEm) && p.getText().toString().equals(dbPw))) {
                        // 데이터와 불일치, 가입 정보 없음
                        pass = false;
                    } else {
                        // 데이터와 일치, 로그인 가능
                        pass = true;
                        break;
                    }
                }
                cursor.close();

                /* 로그인 동작 */
                if (pass) {
                    // 데이터 일치하면, id 값을 전달하며 로그인
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("id", dbEm);
                    //로그인 한 회원 개인의 테이블로 설정하는 과정.
                    // PERSONAL 테이블에 사용자 아이디 값을 넣어준다.
                    db.execSQL("DELETE FROM PERSONAL");
                    db.execSQL("INSERT INTO PERSONAL(id) VALUES ('" + dbEm + "');");
                    startActivity(intent);

                } else {
                    // 일치하지 않으면, 메세지 팝업
                    new AlertDialog.Builder(SignInActivity.this)
                            .setTitle(null)
                            .setMessage("이메일 또는 비밀번호가 일치하지 않습니다." +
                                    "\n입력한 내용을 다시 확인해 주세요.")
                            .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        // 회원가입 버튼 (회원가입 화면으로 이동)
        Button cancel = findViewById(R.id.joinA_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
    }
}