package com.example.cookingdiary02;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//3초가 지나면 HomeActivity로 자동 전환된다.
public class LoadingActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingActivity.this, SignInActivity.class));
            }
        };

        handler.postDelayed(runnable, 3000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}