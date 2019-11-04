package com.example.linshuer.xiangshuo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {
    private Button loginbtn;
    private Button logupbtn;
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String PASSWORD="upass";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn =(Button)findViewById(R.id.loginbtn);
        logupbtn =(Button)findViewById(R.id.logupbtn);
        SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
        String username = sp.getString(USERNAME,null);
        if(username!=null){
            Intent intent1 =new Intent(Login.this,MainActivity.class);
            startActivity(intent1);
        }else {
            loginbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(Login.this,SignIn.class);
                    startActivity(intent);
                }
            });
            logupbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(Login.this,SignUp.class);
                    startActivity(intent);
                }
            });
        }

    }
}
