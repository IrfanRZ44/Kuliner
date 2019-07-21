package com.exomatik.kuliner;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.exomatik.kuliner.Activity.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashScreen extends AppCompatActivity {
    private AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash_screen);

        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avi);

        //handler untuk menunggu selama 2 detik
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {

                Intent localIntent = new Intent(SplashScreen.this, MainActivity.class);
                //berpindah activity
                startActivity(localIntent);
                //menghentikan activity sekarang
                finish();
            }
        }, 2000L);
    }
}
