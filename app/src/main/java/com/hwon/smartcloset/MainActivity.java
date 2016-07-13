package com.hwon.smartcloset;


import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    //전체 레이아웃 구성//
    DrawerLayout drawerLayout;
    //drawer 토글 구성//
    ActionBarDrawerToggle drawerToggle;
    //toolbar//
    Toolbar toolbar;
    TextView title;
    //NavigationView//
    NavigationView navigationView;

    //Image Button//
    ImageButton imgBtnHome;
    ImageButton imgBtnCloset;
    ImageButton imgBtnShop;
    ImageButton imgBtnSearch;

    // Fragment change를 위한 view pager//
    ViewPager viewPager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupView();
        if (savedInstanceState == null) setFragment(0);
    }

    public void setupView(){
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        title = (TextView)findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        //이미지 버튼 생성
        imgBtnHome = (ImageButton) findViewById(R.id.btn_main_home);
        imgBtnCloset = (ImageButton) findViewById(R.id.btn_main_closet);
        imgBtnShop = (ImageButton) findViewById(R.id.btn_main_shop);
        imgBtnSearch = (ImageButton) findViewById(R.id.btn_main_search);
        //기존 타이틀 없애고 새로운 타이틀 생성//
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title.setText("Smart Closet");


        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        //drawerToggle sync맞추면 메뉴 등장!
        drawerToggle.syncState();

        //navigation view 설정 및 Item Select 처리//
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //밑에 있는 onNavigationItemSelected 함수가 this가 됨
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.content_pager);
        MyViewPager adapter = new MyViewPager(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                setFragment(position);
            }

            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    //navigation view menu 선택 처리
    public boolean onNavigationItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.drawer_home:
                setFragment(0);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;

            default:
                break;
        }
        return true;
    }

    //뒤로가기 설정
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    // 이미지 버튼 처리
    public void onClickImageButton(View v){
        switch (v.getId()){
            case R.id.btn_main_home:
                setFragment(0);
                break;
            case R.id.btn_main_search:
                setFragment(1);
                break;
            case R.id.btn_main_shop:
                setFragment(2);
                break;
            case R.id.btn_main_closet:
                setFragment(3);
                break;
        }
    }
    //fragment 변경 처리
    public void setFragment(int position){
        imgBtnHome.setSelected(false);
        imgBtnSearch.setSelected(false);
        imgBtnShop.setSelected(false);
        imgBtnCloset.setSelected(false);
        switch (position){
            case 0:
                viewPager.setCurrentItem(0);
                title.setText("홈");
                imgBtnHome.setSelected(true);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                title.setText("검색");
                imgBtnSearch.setSelected(true);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                title.setText("쇼핑");
                imgBtnShop.setSelected(true);
                break;
            case 3:
                viewPager.setCurrentItem(3);
                title.setText("내 옷장");
                imgBtnCloset.setSelected(true);
                break;
        }
    }
}
