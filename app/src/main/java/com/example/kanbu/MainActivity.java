package com.example.kanbu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // 헤더 아이템
    View header_layout;
    ImageView drawerLayoutClose;

    // 상단 툴바 변수
    private ActionBar actionbar;

    private Toolbar toolbar;

    // 상단 툴바 소메뉴 변수
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        ViewSet();
    }

    // 뷰 세팅 메소드
    public void ViewSet(){
        // 상단 툴바
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 상단 툴바 소메뉴
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //뒤로가기버튼 이미지 적용
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        setNavigationItemSelected(navigationView);

        // 헤더 뷰 추가 및 접근
        header_layout = navigationView.inflateHeaderView(R.layout.main_header_layout);
        drawerLayoutClose = header_layout.findViewById(R.id.iv_NavigationDrawer_close);
        setHeaderLayoutEvent();
    }

    // header_layout 아이템 클릭 이벤트
    public void setHeaderLayoutEvent(){
        
        // drawerLayout 닫기
        drawerLayoutClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawers();
            }
        });
    }

    // navigationView 아이템 선택시 이벤트 세팅
    public void setNavigationItemSelected(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.myCarList:
                        Toast.makeText(mContext,"myCarList", Toast.LENGTH_SHORT).show();
                        item.setCheckable(true);
                        return true;

                    case R.id.connect:
                        Toast.makeText(mContext,"connect", Toast.LENGTH_SHORT).show();
                        item.setCheckable(true);
                        return true;

                    case R.id.setting:
                        Toast.makeText(mContext,"setting", Toast.LENGTH_SHORT).show();
                        item.setCheckable(true);
                        return true;

                }
                return false;
            }
        });
    }

    // 상단 소메뉴 선택시 네비게이션 호출
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}