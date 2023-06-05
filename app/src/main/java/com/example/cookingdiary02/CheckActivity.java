package com.example.cookingdiary02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//등록한 레시피를 바로 확인할 수 있는 화면
public class CheckActivity extends AppCompatActivity {

    private TextView title, content, category, ingredient, writer;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        title = findViewById(R.id.ctitle);
        content = findViewById(R.id.ccontent);
        category = findViewById(R.id.ccategory);
        ingredient = findViewById(R.id.ingredient);
        writer = findViewById(R.id.cwriter);

        Intent intent = getIntent();
        String strTitle = intent.getStringExtra("food");
        String strContent = intent.getStringExtra("recipe");
        String strCategory = intent.getStringExtra("category");
        String strIngredient = intent.getStringExtra("ingredient");

        //DBHelper 생성
        dbHelper = new DBHelper(this);
        //데이터베이스를 읽을 수 있도록 설정
        db = dbHelper.getReadableDatabase();

        //RECIPE 테이블에서 방금 등록한 레시피에 대한 작성자 정보를 불러온다.
        c = db.rawQuery("SELECT * FROM RECIPE ORDER BY num_recipe DESC LIMIT 1", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            writer.setText(c.getString(3));
            c.moveToNext();
        }
        c.close();

        title.setText(strTitle);
        content.setText(strContent);
        category.setText(strCategory);
        ingredient.setText(strIngredient);

        //버튼을 누르면 HomeActivity로 전환된다.
        Button homeButton = (Button) findViewById(R.id.cGoHome);
        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}