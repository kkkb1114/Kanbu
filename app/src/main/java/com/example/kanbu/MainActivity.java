package com.example.kanbu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // 앱 종료 뒤로가기 두번 클릭
    private long backKeyPressedTime = 0;

    // 헤더 아이템
    private View header_layout;
    private ImageView drawerLayoutClose;

    // 상단 툴바 변수
    private ActionBar actionbar;

    private Toolbar toolbar;
    private Button bt_toolbar_connect;
    private TextView tv_toolbar_title;

    // 상단 툴바 소메뉴 변수
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    // 메인 메뉴 프레그 먼트들을 담을 프레임레이아웃
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FrameLayout fl_mainMenu;
    private MainFragment fg_main;
    private ConnectFragment fg_connent;
    private SettingFragment fg_setting;
    private MonitoringFragment fg_monitoring;
    private TerminalFragment fg_terminal;
    private DiagnosisFragment fg_diagnosis;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        ViewSet();
        setFragment();
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

        // 상단 액션바 구성
        bt_toolbar_connect = toolbar.findViewById(R.id.bt_toolbar_connect);
        tv_toolbar_title = toolbar.findViewById(R.id.tv_toolbar_title);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        setNavigationItemSelected(navigationView);

        // 헤더 뷰 추가 및 접근
        header_layout = navigationView.inflateHeaderView(R.layout.main_header_layout);
        drawerLayoutClose = header_layout.findViewById(R.id.iv_NavigationDrawer_close);
        setHeaderLayoutEvent();

        // 프레그먼트 뷰 접근

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

    // 프레그먼트뷰 세팅
    public void setFragment(){
        fragmentManager = getSupportFragmentManager();

        fg_main = new MainFragment();
        fg_connent = new ConnectFragment();
        fg_setting = new SettingFragment();
        fg_monitoring = new MonitoringFragment();
        fg_terminal = new TerminalFragment();
        fg_diagnosis = new DiagnosisFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_mainMenu, fg_main).commitAllowingStateLoss();
    }

    // 프레그먼트 화면 변경 메소드
    public void moveFragment(View view){
        transaction = fragmentManager.beginTransaction();

        switch (view.getId()){
            case R.id.ln_main_monitoring:
                transaction.replace(R.id.fl_mainMenu, fg_monitoring).commitAllowingStateLoss();
                break;
            case R.id.ln_main_diagnosis:
                transaction.replace(R.id.fl_mainMenu, fg_diagnosis).commitAllowingStateLoss();
                break;
            case R.id.ln_main_terminal:
                transaction.replace(R.id.fl_mainMenu, fg_terminal).commitAllowingStateLoss();
                break;
            case R.id.ln_main_setting:
                transaction.replace(R.id.fl_mainMenu, fg_setting).commitAllowingStateLoss();
                break;
            case R.id.tv_toolbar_title:
                transaction.replace(R.id.fl_mainMenu, fg_main).commitAllowingStateLoss();
                break;
            case R.id.bt_toolbar_connect:
                transaction.replace(R.id.fl_mainMenu, fg_connent).commitAllowingStateLoss();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // 기존의 뒤로가기 버튼의 기능 제거
        // super.onBackPressed();

        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2초 이내에 뒤로가기 버튼을 한번 더 클릭시 finish()(앱 종료)
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

}