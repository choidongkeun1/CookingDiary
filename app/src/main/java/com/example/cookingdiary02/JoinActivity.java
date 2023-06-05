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

public class JoinActivity extends AppCompatActivity {

    TextInputLayout layoutName, layoutEmail, layoutPw, layoutPwck;
    String name, email, pw, pwck;
    String dbEm = "";
    DBHelper dbHelper;
    boolean emCheck = true;  //이메일 사용가능 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // DBHelper 생성
        if (dbHelper == null) {
            dbHelper = new DBHelper(this);
        }
        // 데이터베이스 읽고 쓸 수 있도록 설정
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // Text 입력창 가리키는 변수
        layoutName = findViewById(R.id.layout_name);
        layoutEmail = findViewById(R.id.layout_email);
        layoutPw = findViewById(R.id.layout_pw);
        layoutPwck = findViewById(R.id.layout_pwck);

        // 화면에 입력받은 값 가져오기
        EditText n = findViewById(R.id.join_name);
        EditText e = findViewById(R.id.join_email);
        EditText p = findViewById(R.id.join_pw);
        EditText pc = findViewById(R.id.join_pwck);
        name = n.getText().toString();
        email = e.getText().toString();
        pw = p.getText().toString();
        pwck = pc.getText().toString();

        // 회원가입 버튼
        Button joinB = findViewById(R.id.joinB_button);
        // 회원정보 입력 전 - 버튼 비활성화
        if (name.isEmpty() || email.isEmpty() || pw.isEmpty() || pwck.isEmpty()) {
            joinB.setClickable(false);
            joinB.setEnabled(false);
        }

        // 이름 입력창 내용 변화 감지
        n.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 이름 조건 만족하지 않은 경우 오류
                if (s.toString().isEmpty() || s.length() < 2) {
                    layoutName.setError("필수 정보입니다.");
                } else {
                    layoutName.setError(null);
                    // 다른 입력창 오류 시 버튼 비활성화 유지
                    if (e.getText().toString().isEmpty()
                            || !e.getText().toString().matches("(.*)@(.*)+\\.(.*)")
                            || p.getText().toString().isEmpty() || p.length() < 6
                            || !pc.getText().toString().equals(p.getText().toString())) {
                        joinB.setClickable(false);
                        joinB.setEnabled(false);
                    } else {  // 버튼 활성화
                        joinB.setClickable(true);
                        joinB.setEnabled(true);
                    }
                }
            }
        });

        // 이메일 입력창 내용 변화 감지
        e.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 이메일 조건 만족하지 않은 경우 오류
                if (s.toString().isEmpty() || !s.toString().matches("(.*)@(.*)+\\.(.*)")) {
                    layoutEmail.setError("올바른 이메일을 입력해주세요.");
                } else {
                    layoutEmail.setError(null);
                    // 다른 입력창 오류 시 버튼 비활성화 유지
                    if (n.getText().toString().isEmpty() || n.length() < 2
                            || p.getText().toString().isEmpty() || p.length() < 6
                            || !pc.getText().toString().equals(p.getText().toString())) {
                        joinB.setClickable(false);
                        joinB.setEnabled(false);
                    } else {  // 버튼 활성화
                        joinB.setClickable(true);
                        joinB.setEnabled(true);
                    }
                }
            }
        });

        // 비밀번호 입력창 내용 변화 감지
        p.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 비밀번호 조건 만족하지 않은 경우 오류
                if (s.toString().isEmpty() || s.length() < 6) {
                    layoutPw.setError("비밀번호를 6자리 이상 입력해주세요.");
                } else {
                    layoutPw.setError(null);
                    // 다른 입력창 오류 시 버튼 비활성화 유지
                    if (n.getText().toString().isEmpty() || n.length() < 2
                            || e.getText().toString().isEmpty()
                            || !e.getText().toString().matches("(.*)@(.*)+\\.(.*)")
                            || !pc.getText().toString().equals(p.getText().toString())) {
                        joinB.setClickable(false);
                        joinB.setEnabled(false);
                    } else {  // 버튼 활성화
                        joinB.setClickable(true);
                        joinB.setEnabled(true);
                    }
                }
            }
        });

        // 비밀번호 확인 입력창 내용 변화 감지
        pc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 비밀번호 확인 조건 만족하지 않은 경우 오류
                if (!s.toString().equals(p.getText().toString())) {
                    layoutPwck.setError("비밀번호가 일치하지 않습니다.");
                } else {
                    layoutPwck.setError(null);
                    // 다른 입력창 오류 시 버튼 비활성화 유지
                    if (n.getText().toString().isEmpty() || n.length() < 2
                            || e.getText().toString().isEmpty()
                            || !e.getText().toString().matches("(.*)@(.*)+\\.(.*)")
                            || p.getText().toString().isEmpty() || p.length() < 6) {
                        joinB.setClickable(false);
                        joinB.setEnabled(false);
                    } else {  // 버튼 활성화
                        joinB.setClickable(true);
                        joinB.setEnabled(true);
                    }
                }
            }
        });

        // 회원가입 버튼 동작
        joinB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* 이메일 중복 확인하기 */
                if (db != null) {
                    // DB의 ID값 읽기
                    Cursor cursor = db.rawQuery("SELECT ID FROM INFO", null);
                    while (cursor.moveToNext()) {
                        dbEm = cursor.getString(0);

                        // 입력정보와 데이터 비교
                        if (!e.getText().toString().equals(dbEm)) {
                            // 이메일 사용가능
                            emCheck = true;
                        } else {
                            // 이메일 중복
                            emCheck = false;
                            break;
                        }
                    }
                    cursor.close();
                } else
                    // DB가 null 이면 이메일 사용가능
                    emCheck = true;

                /*  회원가입 동작  */
                if (emCheck) {
                    // 이메일 사용가능 시 DB에 회원정보 저장
                    assert db != null;
                    db.execSQL("INSERT INTO INFO VALUES ('" + e.getText().toString() + "', '"
                            + p.getText().toString() + "', '" + n.getText().toString() + "');");

                    // 가입 완료 팝업 - '확인' 클릭 시 로그인 화면으로 이동
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle(null)
                            .setMessage("회원가입이 완료되었습니다.")
                            .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .show();

                } else {
                    // 이메일이 중복된 경우 - 중복 알림 팝업
                    new AlertDialog.Builder(JoinActivity.this)
                            .setTitle(null)
                            .setMessage("이미 가입된 이메일입니다.")
                            .setNeutralButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        // 취소 버튼 (로그인 화면으로 이동)
        Button cancel = findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }
}