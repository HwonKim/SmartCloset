package com.hwon.smartcloset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.os.Handler;

/**
 * Created by hwkim_000 on 2016-07-11.
 */
public class SplashActivity extends AppCompatActivity {
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd = new Handler();
        //3초 후 MainActivity 실행하고 SplashActivity 종료.
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplication(), IntroActivity.class));
                SplashActivity.this.finish();
            }
        }, 3000);
    }
}
