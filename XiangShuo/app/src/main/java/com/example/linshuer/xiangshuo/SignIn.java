package com.example.linshuer.xiangshuo;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/*import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;*/
import com.example.linshuer.xiangshuo.bean.User;
import com.example.linshuer.xiangshuo.db.UserHelper;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;
import com.google.gson.Gson;

public class SignIn extends AppCompatActivity {
    private UserHelper userHelper;
    private SQLiteDatabase db;
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String PASSWORD="upass";
    private final static String THEME="utheme";
    private final static String NICKNAME="unickname";
    private final String HOST_PATH="http://192.168.0.111:8080";
    private final String HEADIMAGE_URL="http://192.168.0.111:8080/APPService/headimage/";
/*    private final static String ADDRESS="address";*/
    private Button loginbtn;
    private EditText username;
    private EditText password;
    private ImageView back;
    //private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        //========================
        userHelper = new UserHelper(this);
        db =userHelper.getWritableDatabase();
        //========================
        back =(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn.this.finish();
            }
        });
        //======================
        //requestQueue = Volley.newRequestQueue(this);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("")&&password.getText().toString().equals("")){
                    Toast.makeText(SignIn.this,"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    MyTask myTask = new MyTask();
                    myTask.execute(username.getText().toString(),password.getText().toString());
                }
            }
        });
        //
        getSignUpbudle();//设置注册号的信息
    }
    //保存到sharePreferences
    public void saveUser(String username,String password){
        SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USERNAME,username);
        editor.putString(PASSWORD,password);
        editor.commit();
    }
//====存入数据库
    public void saveUserAsDB(String userJson){
        Gson gson = new Gson();
        User user = gson.fromJson(userJson,User.class);
        Cursor c=db.query("user",new String[]{},"username='"+user.getUsername()+"'",null,null,null,null);
        ContentValues value=new ContentValues();
        value.put("username",user.getUsername() );
        value.put("password",user.getPassword());
        value.put("headimage",user.getHeadimage() );
        value.put("nickname", user.getNickname());
        value.put("sex", user.getSex());
        value.put("address", user.getAddress());
        value.put("autograph",user.getAutopraph());
        value.put("theme",user.getTheme());
        SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(THEME,user.getTheme());
        editor.putString(NICKNAME,user.getNickname());
        editor.commit();
        if(c.moveToFirst()) {
            db.update("user", value, "username='" + user.getUsername()+"'", null);
        }else{
            db.insert("user", null, value);
        }
        db.close();
    }
    //============================
    public void getSignUpbudle(){
            if(getIntent().getStringExtra("username")!=null&&getIntent().getStringExtra("password")!=null){
                username.setText(getIntent().getStringExtra("username"));
                password.setText(getIntent().getStringExtra("password"));
            }
    }
//============================
    class MyTask extends AsyncTask<String,Integer,String> {
        public MyTask(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("error")||s==null){//保存到数据库
                Toast.makeText(SignIn.this, "密码或账号错误", Toast.LENGTH_SHORT).show();
            }else if(s.equals("outtime")){
                Toast.makeText(SignIn.this,"网络超时",Toast.LENGTH_SHORT).show();
            }else {
                saveUser(username.getText().toString(),password.getText().toString());
                saveUserAsDB(s);//保存到本地数据库
                Intent intent =new Intent(SignIn.this,MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="username="+strings[0]+"&password="+strings[1];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/signInAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }
}
