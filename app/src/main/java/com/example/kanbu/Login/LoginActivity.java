package com.example.kanbu.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.kanbu.Main.MainActivity;
import com.example.kanbu.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    // 파이어베이스 구글 로그인 필요 변수
    private FirebaseAuth firebaseAuth = null;
    private GoogleSignInClient googleSignInClient = null;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton SignInButton;

    //앱 이름 텍스트
    TextView tv_APPtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // 뷰 세팅 메소드
        ViewSet();

        // 구글 로그인 세팅
        GoogleSetting();

        // 페이스북 로그인 세팅
        //FacebookSetting();

        // 구글 로그인 성공시 메인화면 이동 및 화면 finish()
        if (firebaseAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }



    // 뷰 세팅 메소드
    public void ViewSet(){
        tv_APPtitle = findViewById(R.id.tv_APPtitle);
        tv_APPtitle.setOnClickListener(this);
        SignInButton = findViewById(R.id.SignInButton);
        SignInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SignInButton:
                GoogleSignIn();
                break;
            case R.id.tv_APPtitle:
                break;
        }
    }

    public void GoogleSetting(){
        // 파이어베이스인증(firebaseAuth)에게서 firebaseAuth를 사용하기 위해 인스턴스를 얻는다.
        firebaseAuth = FirebaseAuth.getInstance();

        // GoogleSignInOptions을 이용하여 유저의 토큰을 받아내고 유저 이메일을 받는다. 파이어베이스를 통해 인증된 토큰을 얻는 것 같다.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("232040673356-uabjgcfso7c9a4ftsni2u6vjosufcudd.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    public void GoogleSignIn(){
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // GoogleSignInApi.getSignInIntent(...)에서 인텐트를 실행하여 반환된 결과;
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // 구글 로그인 성공, Firebase 인증
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()){
                            // 로그인 성공, 로그인한 사용자 정보로 UI 업데이트
                            Snackbar.make(findViewById(android.R.id.content), "Authentication Successed.", Snackbar.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        }else {
                            // 로그인에 실패하면 사용자에게 메시지를 표시합니다.
                            Snackbar.make(findViewById(android.R.id.content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    }