package com.example.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {
    private EditText edtName;
    private EditText edtPass;
    private Button btnLogin;
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("用户登录");

        edtName = findViewById(R.id.edt_name);
        edtPass = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        btnReg = findViewById(R.id.btn_reg);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();
                String pass = edtPass.getText().toString();
                BmobUser.loginByAccount(name, pass, new LogInListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e == null){
                            Toast.makeText(LoginActivity.this, "登录成功 " +
                                    bmobUser.getUsername(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }
}
