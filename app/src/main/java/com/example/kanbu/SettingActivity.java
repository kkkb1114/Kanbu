package com.example.kanbu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    // Google 인증 변수
    FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    TextView Logout;
    TextView secession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ViewSet();

        firebaseAuth = FirebaseAuth.getInstance();
        // GoogleSignInOptions을 이용하여 유저의 토큰을 받아내고 유저 이메일을 받는다. 파이어베이스를 통해 인증된 토큰을 얻는 것 같다.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // 프로젝트에서 build/generated/res/google-services/debug/values/values.xml를 뒤져보면 default_web_client_id가 있다.
                // 해당 id의 값을 복사하여 토큰 요청을 한다.
                .requestIdToken("232040673356-uabjgcfso7c9a4ftsni2u6vjosufcudd.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

    }

    // 뷰 세팅 메소드
    public void ViewSet(){
        Logout = findViewById(R.id.Logout);
        Logout.setOnClickListener(this);
        secession = findViewById(R.id.secession);
        secession.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Logout:
                GoogleLogout();
                // 앱 완전 종료
                finishAffinity();
                break;
            case R.id.secession:
                GoogleSecession();
                // 앱 완전 종료
                finishAffinity();
                break;
        }
    }

    public void GoogleLogout(){
        firebaseAuth.signOut();
        // 재 로그인할때 다시 구글 아이디 리스트를 띄우기 위함.
        // 이렇게 안하면 전에 로그인했던 기록이 남아 처음에 로그인했던 아이디로 자동 로그인 시도된다.
        if (googleSignInClient != null){
            googleSignInClient.signOut();
        }
    }

    public void GoogleSecession(){
        firebaseAuth.getCurrentUser().delete();
        // 재 로그인할때 다시 구글 아이디 리스트를 띄우기 위함.
        // 이렇게 안하면 전에 로그인했던 기록이 남아 처음에 로그인했던 아이디로 자동 로그인 시도된다.
        if (googleSignInClient != null){
            googleSignInClient.signOut();
        }
    }
}