package com.example.cookingdiary02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

//레시피 목록을 보여주고 검색할 수 있는 화면
public class HomeActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c1, c2;
    ListView list = null;
    ListItemAdapter adapter;
    String wid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String strId = intent.getStringExtra("id");

        //리스트뷰 참조
        list = (ListView)findViewById(R.id.list);

        //Adapter 생성
        adapter = new ListItemAdapter();

        //DBHelper 생성
        if(dbHelper == null) {
            dbHelper = new DBHelper(this);
        }

        //데이터베이스를 읽을 수 있도록 설정
        db = dbHelper.getReadableDatabase();

        //RECIPE 테이블을 읽고 리스트 아이템에 각각 알맞는 데이터 값을 넣어준다.
        c2 = db.rawQuery("SELECT * FROM RECIPE",null);
        c2.moveToFirst();
        while (!c2.isAfterLast()) {
            adapter.addItem(c2.getInt(0),c2.getString(1),c2.getString(3));
            c2.moveToNext();
        }
        //리스트뷰에 Adapter 달기
        list.setAdapter(adapter);
        c2.close();

        //필터를 적용할 EditText 설정
        EditText editTextFilter = (EditText)findViewById(R.id.search);
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString();


                //필터링 사용 시 팝업 텍스트 표시하지 않기
                ((ListItemAdapter)list.getAdapter()).getFilter().filter(filterText);
            }

            @Override
            public void beforeTextChanged(CharSequence cs, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
            }
        });

        //리스트뷰 클릭 이벤트 설정
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override //리스트뷰 아이템 클릭 시 아이템에 맞는 상세 화면을 보여주도록 한다.
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                int reciNum = ((ListItem)adapter.getItem(position)).getTxt1();
                String reciFood = ((ListItem)adapter.getItem(position)).getTxt2();
                String reciWriter = ((ListItem)adapter.getItem(position)).getTxt3();
                Intent intent = new Intent(getApplication(), DetailActivity.class);   //DetailActivity 클래스를 불러온다
                intent.putExtra("recinum", reciNum);
                intent.putExtra("recifood",reciFood);
                intent.putExtra("reciwriter", reciWriter);
                startActivity(intent);

            }
        });

        //버튼을 누르면 InputActivity로 전환된다.
        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, InputActivity.class);    // HomeActivity, InputActivity를 불러온다
                startActivity(intent);
            }
        });

        //버튼을 누르면 MypageActivity로 전환된다.
        Button mypageButton = (Button) findViewById(R.id.goMyPage);
        mypageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });
    }
}