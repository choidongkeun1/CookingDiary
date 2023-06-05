package com.example.cookingdiary02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//HomeActivity에서 리스트 아이템을 클릭하면 상세 레시피를 보여주는 화면
public class DetailActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c1,c2,c3;
    TextView food, writer, recipe, ingredient, category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        int intNum = intent.getIntExtra("recinum",1);

        food = (TextView)findViewById(R.id.title);
        writer = (TextView)findViewById(R.id.writer);
        recipe = (TextView)findViewById(R.id.content);
        ingredient = (TextView)findViewById(R.id.ingredient);
        category = (TextView)findViewById(R.id.category);

        //DBHelper 생성
        dbHelper = new DBHelper(this);
        //데이터베이스를 읽을 수 있도록 설정
        db = dbHelper.getReadableDatabase();

        //RECIPE 테이블에서 선택한 리스트 아이템의 작성자, 요리법, 음식 값을 불러온다.
        c1 = db.rawQuery("SELECT * FROM RECIPE WHERE num_recipe=" + intNum + "", null);
        c1.moveToFirst();
        while (!c1.isAfterLast()) {
            writer.setText(c1.getString(3));
            recipe.setText(c1.getString(2));
            food.setText(c1.getString(1));
            c1.moveToNext();
        }
        c1.close();

        //INGREDIENT 테이블에서 선택한 리스트 아이템의 재료 값을 불러온다.
        c2 = db.rawQuery("SELECT * FROM INGREDIENT WHERE num_recipe=" + intNum + "", null);
        c2.moveToFirst();
        while (!c2.isAfterLast()) {
            ingredient.setText(c2.getString(1));
            c2.moveToNext();
        }
        c2.close();

        //FOOD 테이블에서 선택한 리스트 아이템의 음식 카테고리 값을 불러온다.
        c3 = db.rawQuery("SELECT * FROM FOOD WHERE num_recipe=" + intNum + "", null);
        c3.moveToFirst();
        while (!c3.isAfterLast()) {
            category.setText(c3.getString(2));
            c3.moveToNext();
        }
        c3.close();

        //버튼을 누르면 HomeActivity로 전환된다.
        Button homeButton = (Button) findViewById(R.id.goHome);
        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}