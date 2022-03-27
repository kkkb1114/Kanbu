package com.example.kanbu.Main;

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

import com.example.kanbu.Diagnosis.DiagnosisFragment;
import com.example.kanbu.Monitoring.MonitoringFragment;
import com.example.kanbu.OBD_Connect.ConnectFragment;
import com.example.kanbu.R;
import com.example.kanbu.Setting.SettingFragment;
import com.example.kanbu.DPF_Monitoring.DPF_MonitoringFragment;
import com.example.kanbu.onBackPressedListener;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

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
    private DPF_MonitoringFragment fg_terminal;
    private DiagnosisFragment fg_diagnosis;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        // MainActivity에 있는 뷰 세팅
        ViewSet();
        // 프래그먼트 세팅
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
                        moveFragmentSupport(new DPF_MonitoringFragment(), drawerLayout);
                        //moveFragmentSupport(new MonitoringFragment(), drawerLayout);
                        return true;

                    case R.id.connect:
                        moveFragmentSupport(new ConnectFragment(), drawerLayout);
                        return true;

                    case R.id.setting:
                        moveFragmentSupport(new SettingFragment(), drawerLayout);
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
        fg_terminal = new DPF_MonitoringFragment();
        fg_diagnosis = new DiagnosisFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_mainMenu, fg_main).commitAllowingStateLoss();
    }

    // getSupportFragmentManager로 FragmentManager에 접근하여 addToBackStack로 프래그먼트 화면 스택을 쌓아준다. (뒤로가기 버튼 눌렀을때 바로 전 프래그먼트 화면을 띄우기 위해)
    // 프레그먼트 화면 변경 메소드
    public void moveFragment(View view){
        transaction = fragmentManager.beginTransaction();

        switch (view.getId()){
            case R.id.ln_main_monitoring:
                moveFragmentSupport(new MonitoringFragment(), null);
                break;
            case R.id.ln_main_diagnosis:
                moveFragmentSupport(new DiagnosisFragment(), null);
                break;
            case R.id.ln_main_terminal:
                moveFragmentSupport(new DPF_MonitoringFragment(), null);
                break;
            case R.id.ln_main_setting:
                moveFragmentSupport(new SettingFragment(), null);
                break;
            case R.id.bt_toolbar_connect:
                moveFragmentSupport(new ConnectFragment(), null);
                break;
        }
    }

    public void moveFragmentSupport(Fragment fragment, DrawerLayout drawerLayout){
        if (drawerLayout == null){
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainMenu, fragment)
                    .addToBackStack(null).commit();
        }else {
            this.getSupportFragmentManager().beginTransaction().replace(R.id.fl_mainMenu, fragment)
                    .addToBackStack(null).commit();
            drawerLayout.closeDrawers();
        }
    }


    //todo 모든 프래그먼트는 전부 MainActivity에 쌓이는 것이기에 onBackPressed를 Override한 후 onBackPressed가 실행될때마다 프레그먼트 스택이 한번씩 돌게 만든다. (이게 맞는지는 아직 부정확함.)
    @Override
    public void onBackPressed() {

        // 프래그먼트 onBackPressedListener사용
        // getSupportFragmentManager()를 이용하면 FragmentManager에 접근이 가능하다.
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragmentList){
            //todo instanceof 연산자는 객체가 어떤 클래스인지, 어떤 클래스를 상속 받았는지 확인하는데 사용하는 연산자이다.
            // 그럼으로 fragment가 onBackPressedListener를 상속 받았다면?? 이라는 조건문이 된다.
            if (fragment instanceof onBackPressedListener){
                ((onBackPressedListener)fragment).onBackPressed();
                return;
            }
        }

        // 기존의 뒤로가기 버튼의 기능 제거 (오버라이드)
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