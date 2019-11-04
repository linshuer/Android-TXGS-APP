package com.example.linshuer.xiangshuo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.linshuer.xiangshuo.httpcon.HttpUtils;

public class SignUp extends AppCompatActivity {
    private ImageView back;
    private EditText username;
    private EditText password;
    private Button logupbtn;
    private final String HOST_PATH="http://192.168.0.111:8080";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        back =(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp.this.finish();
            }
        });
        //================
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        logupbtn = (Button) findViewById(R.id.logupbtn);
        logupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("")&&password.getText().toString().equals("")){
                    Toast.makeText(SignUp.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    MyTask myTask = new MyTask();
                    myTask.execute(username.getText().toString(),password.getText().toString());
                }
            }
        });
    }
    class MyTask extends AsyncTask<String,Integer,String> {
        public MyTask(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("outtime")){
                Toast.makeText(SignUp.this,"网络超时",Toast.LENGTH_SHORT).show();
            }else {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                intent.putExtra("username", username.getText().toString());
                intent.putExtra("password", password.getText().toString());
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="username="+strings[0]+"&password="+strings[1];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/signUpAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }
}
