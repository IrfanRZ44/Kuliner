package com.exomatik.kuliner.Activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.exomatik.kuliner.R;

public class MainActivity extends AppCompatActivity {
    private TextView btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (TextView) findViewById(R.id.text_sign_in);

        btnSignIn.setPaintFlags(btnSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setTextColor(getResources().getColor(R.color.green3));
                startActivity(new Intent(MainActivity.this, ActSignIn.class));
                finish();
            }
        });
    }
}
