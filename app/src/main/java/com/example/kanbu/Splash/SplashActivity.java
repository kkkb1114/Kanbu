package com.example.kanbu.Splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.kanbu.Main.MainActivity;
import com.example.kanbu.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        moveNext(1);
    }

    public void moveNext(int sec){
        try {

            Thread.sleep(1000 * sec);
        }catch (InterruptedException e){

        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}