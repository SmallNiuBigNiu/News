package com.example.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegActivity extends AppCompatActivity {

    private EditText edtName;
    private EditText edtPass;
    private Button btnReg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        setTitle("注册页面");


        edtName = findViewById(R.id.edt_name);
        edtPass = findViewById(R.id.edt_pass);
        btnReg = findViewById(R.id.btn_reg);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtName.getText().toString();
                String pass = edtPass.getText().toString();
                BmobUser bmobUser = new BmobUser();
                bmobUser.setUsername(userName);
                bmobUser.setPassword(pass);
                bmobUser.signUp(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e == null){
                            Toast.makeText(RegActivity.this, " 注册成功 " + bmobUser.getUsername(),
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
        });
    }
}
