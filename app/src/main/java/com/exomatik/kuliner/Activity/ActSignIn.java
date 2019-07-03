package com.exomatik.kuliner.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.exomatik.kuliner.R;

public class ActSignIn extends AppCompatActivity {
    private EditText etPhone, etPassword;
    private Button btnSignIn;
    private View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_in);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        etPhone = (EditText) findViewById(R.id.et_nomor);
        etPassword = (EditText) findViewById(R.id.et_password);
        v = (View) findViewById(android.R.id.content);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekEditText();
            }
        });
    }

    private void cekEditText() {
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        if (phone.isEmpty() || password.isEmpty()){
            if (phone.isEmpty()){
                etPhone.setError(getResources().getString(R.string.error_data_kosong));
            }
            if (password.isEmpty()){
                etPassword.setError(getResources().getString(R.string.error_data_kosong));
            }
        }
        else {
            if (phone.equals("Admin") && password.equals("123456")){
                startActivity(new Intent(ActSignIn.this, ActMenuAdmin.class));
                finish();
            }
            else {
                Snackbar snackbar = Snackbar
                        .make(v, "User dan Password salah", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActSignIn.this, MainActivity.class));
        finish();
    }
}
