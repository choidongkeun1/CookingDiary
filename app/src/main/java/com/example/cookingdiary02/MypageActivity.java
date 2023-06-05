package com.example.cookingdiary02;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MypageActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c, c1, c2;
    ListView list = null;
    ListItemAdapter adapter;
    TextView count, id;
    int round;
    String strId;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //리스트뷰 참조
        list = (ListView)findViewById(R.id.list2);

        //Adapter 생성
        adapter = new ListItemAdapter();

        //DBHelper 생성
        dbHelper = new DBHelper(this);

        //데이터베이스를 읽을 수 있도록 설정
        db = dbHelper.getReadableDatabase();

        //PERSONAL 테이블에서 사용자 id 값을 받아온다.
        c = db.rawQuery("SELECT * FROM PERSONAL", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            strId=c.getString(0);
            c.moveToNext();
        }
        c.close();

        count = (TextView)findViewById(R.id.count);
        id = (TextView)findViewById(R.id.id);
        id.setText(strId);

        /*RECIPE 테이블을 앞에서 불러온 사용자 id 값의 조건에 맞게 읽고
        리스트 아이템에 각각 알맞는 데이터 값을 넣어준다.*/
        c1 = db.rawQuery("SELECT * FROM RECIPE WHERE id=?", new String[]{strId});
        c1.moveToFirst();
        while (!c1.isAfterLast()) {
            round=c1.getCount(); //사용자가 작성한 게시물의 수를 round에 저장한다.
            adapter.addItem(c1.getInt(0),c1.getString(1),c1.getString(3));
            c1.moveToNext();
        }
        //리스트뷰에 Adapter 달기
        list.setAdapter(adapter);
        c1.close();

        //PERSONAL 테이블의 count(게시물 수)를 업데이트 해준다.
        db.execSQL("UPDATE PERSONAL SET count=" + round + ";");

        //PERSONAL 테이블에서 count를 불러와 마이페이지 화면에 게시물 수로 보여준다.
        c2 = db.rawQuery("SELECT * FROM PERSONAL", null);
        c2.moveToFirst();
        while (!c2.isAfterLast()) {
            count.setText(String.valueOf(c2.getInt(1)));
            c2.moveToNext();
        }
        c2.close();


        //리스트뷰 클릭 이벤트 설정
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override //리스트뷰 아이템 클릭 시 아이템에 맞는 상세 화면을 보여주도록 한다.
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                int reciNum = ((ListItem)adapter.getItem(position)).getTxt1();
                String reciFood = ((ListItem)adapter.getItem(position)).getTxt2();
                String reciWriter = ((ListItem)adapter.getItem(position)).getTxt3();
                Intent intent = new Intent(getApplication(), DetailActivity.class);
                intent.putExtra("recinum", reciNum);
                intent.putExtra("recifood",reciFood);
                intent.putExtra("reciwriter", reciWriter);
                startActivity(intent);

            }
        });
    }
}