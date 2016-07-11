package com.hwon.smartcloset;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toolbar Setting//
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        TextView title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("로그인");
        //뒤로가기 화살표//
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //메뉴 선택//
    public boolean onOptionsItemSelected(MenuItem item) {
        //뒤로가기 적용//
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
