package com.example.cookingdiary02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

//레시피를 등록할 수 있는 화면
public class InputActivity extends AppCompatActivity {

    CheckBox ch1, ch2, ch3, ch4, ch5, ch6, ch7;
    EditText inputFood, inputRecipe, inputIngredient;
    Button completeButton;
    DBHelper dbHelper;
    SQLiteDatabase db;
    String strId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        ch1 = (CheckBox)findViewById(R.id.korean);
        ch2 = (CheckBox)findViewById(R.id.casual);
        ch3 = (CheckBox)findViewById(R.id.fast);
        ch4 = (CheckBox)findViewById(R.id.western);
        ch5 = (CheckBox)findViewById(R.id.japanese);
        ch6 = (CheckBox)findViewById(R.id.chinese);
        ch7 = (CheckBox)findViewById(R.id.others);

        inputFood = (EditText) findViewById(R.id.inputFood);
        inputRecipe = (EditText) findViewById(R.id.inputRecipe);
        inputIngredient = (EditText) findViewById(R.id.inputIngredient);


        completeButton = (Button) findViewById(R.id.complete);
        completeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //DBHelper 생성
                dbHelper = new DBHelper(InputActivity.this);
                //데이터베이스에 쓸 수 있도록 설정
                db = dbHelper.getWritableDatabase();

                //체크박스 선택에 따라 strCategory에 해당 체크박스 값 저장
                String strCategory = "";
                if(ch1.isChecked() == true)
                    strCategory += ch1.getText().toString();
                if(ch2.isChecked() == true)
                    strCategory += ch2.getText().toString();
                if(ch3.isChecked() == true)
                    strCategory += ch3.getText().toString();
                if(ch4.isChecked() == true)
                    strCategory += ch4.getText().toString();
                if(ch5.isChecked() == true)
                    strCategory += ch5.getText().toString();
                if(ch6.isChecked() == true)
                    strCategory += ch6.getText().toString();
                if(ch7.isChecked() == true)
                    strCategory += ch7.getText().toString();

                String strFood = inputFood.getText().toString();
                String strRecipe = inputRecipe.getText().toString();
                String strIngredient = inputIngredient.getText().toString();
                Cursor c = db.rawQuery("SELECT * FROM PERSONAL", null);
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    strId=c.getString(0);
                    c.moveToNext();
                }
                c.close();

                //데이터베이스 RECIPE 테이블에 음식, 요리법, 작성자 삽입
                db.execSQL("INSERT INTO RECIPE(food, id, recipe) " +
                        "VALUES ('" + strFood + "', '" + strId + "', '" + strRecipe + "');");
                //데이터베이스 INGREDIENT 테이블에 재료 삽입
                db.execSQL("INSERT INTO INGREDIENT(ingredient) VALUES ('" + strIngredient + "');");
                //데이터베이스 FOOD 테이블에 음식, 음식 카테고리 삽입
                db.execSQL("INSERT INTO FOOD (food, fcategory) " +
                        "VALUES ('" + strFood + "', '" + strCategory + "');");

                Intent intent = new Intent(InputActivity.this, CheckActivity.class);
                intent.putExtra("food", strFood);
                intent.putExtra("recipe", strRecipe);
                intent.putExtra("category", strCategory);
                intent.putExtra("ingredient", strIngredient);
                intent.putExtra("wid",strId);
                startActivity(intent);
            }
        });

    }
}