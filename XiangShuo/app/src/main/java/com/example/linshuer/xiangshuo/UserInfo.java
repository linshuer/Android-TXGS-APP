package com.example.linshuer.xiangshuo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.linshuer.xiangshuo.bean.User;
import com.example.linshuer.xiangshuo.db.UserHelper;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;

public class UserInfo extends AppCompatActivity {
    private ImageView back,u_headimage,sex_ico,theme_ico;
    private EditText u_nickname,u_address,u_autopraph;
    private TextView alertbtn,signoutbtn;
    private UserHelper userHelper;
    private SQLiteDatabase db;
    private Spinner u_sex;
    private Spinner u_theme;
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String PASSWORD="upass";
    private final static String THEME="utheme";
    private final static String NICKNAME="unickname";
    private final String HOST_PATH="http://192.168.0.109:8080";
    private final String HEADIMAGE_URL="http://192.168.0.109:8080/APPService/headimage/";
    //上传的servlet
    public static final String UPLOAD_URL="http://192.168.0.109:8080/APPService/uploadImageAction";
    //
    private static String head_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiangshuo/headimage/";
    //
    private Bitmap mDownloadImage;
    private String alter_sex=null;
    private String alter_theme=null;//用于修改
    //====================
    private final int PHOTO_REQUEST_GALLERY=1;
    private final int PHOTO_REQUEST_CUT=2;
    public Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/"+"temphead.png"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userHelper = new UserHelper(this);
        db =userHelper.getWritableDatabase();
        u_nickname =(EditText)findViewById(R.id.nickname);
        u_address =(EditText)findViewById(R.id.address);
        u_autopraph =(EditText)findViewById(R.id.autopraph);
        alertbtn =(TextView)findViewById(R.id.alertbtn);
        signoutbtn =(TextView)findViewById(R.id.signoutbtn);
        u_sex =(Spinner)findViewById(R.id.sex_msg);
        u_theme =(Spinner)findViewById(R.id.theme_msg);
        back =(ImageView)findViewById(R.id.back);
        u_headimage=(ImageView)findViewById(R.id.headimage);
        sex_ico=(ImageView)findViewById(R.id.sex_ico);
        theme_ico=(ImageView)findViewById(R.id.theme_ico);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo.this.finish();
            }
        });
        //
        alertbtn.setTextColor(Color.parseColor("#ffffff"));
        alertbtn.setEnabled(false);
        //修改
        alertbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterUserAsDB();
                InfoMyTask infoMyTask = new InfoMyTask();
                infoMyTask.execute(getAlterUser());
            }
        });
        //初始化信息
        setUserInfo();
        //控件监听
        initEditListener();
        //sex
        u_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sInfo=adapterView.getItemAtPosition(i).toString();
                alter_sex =sInfo;
                if(sInfo.equals("男")){
                    sex_ico.setImageResource(R.mipmap.boy);
                }else{
                    sex_ico.setImageResource(R.mipmap.girl);
                }
                alertbtn.setEnabled(true);
                alertbtn.setTextColor(Color.parseColor("#1296db"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //theme
        u_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String theme=adapterView.getItemAtPosition(i).toString();
                alter_theme =theme;
                if(theme.equals("蓝色")){
                    theme_ico.setImageResource(R.mipmap.theme_blue);
                }else if(theme.equals("紫色")){
                    theme_ico.setImageResource(R.mipmap.themepurple);
                }else if(theme.equals("绿色")){
                    theme_ico.setImageResource(R.mipmap.themegreen);
                }
                alertbtn.setEnabled(true);
                alertbtn.setTextColor(Color.parseColor("#1296db"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //退出登录
        signoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserInfo.this);
                builder.setIcon(android.R.drawable.ic_dialog_info);
               /* builder.setTitle("温馨提示");*/
                builder.setMessage("是否退出登录？");
                builder.setCancelable(true);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sp = getSharedPreferences(SP_INFO, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent =new Intent(UserInfo.this,Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
        /*
         *  在这里实现你自己的逻辑
         */
                    }
                });
                builder.create().show();
            }
        });
        //==================================头像
        u_headimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery();
            }
        });

    }

        //设置信息
    public void setUserInfo(){
        SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
        String username = sp.getString(USERNAME,null);
        if (username!=null){

            Cursor c=db.query("user",new String[]{},"username='"+username+"'",null,null,null,null);
            if(c.moveToFirst()==false) {
                Toast.makeText(UserInfo.this,"加载信息失败.",Toast.LENGTH_SHORT).show();

            }else{
                String address=c.getString(c.getColumnIndex("address"));
                String nickname=c.getString(c.getColumnIndex("nickname"));
                String sex=c.getString(c.getColumnIndex("sex"));
                String autopraph=c.getString(c.getColumnIndex("autograph"));
                String headimage =c.getString(c.getColumnIndex("headimage"));
                String theme =c.getString(c.getColumnIndex("theme"));
                u_nickname.setText(nickname);
                u_autopraph.setText(autopraph);
                u_address.setText(address);
                //性别
                setSpinnerItemSelectedByValue(u_sex,sex);
                alter_sex=sex;
                if(sex.equals("男")){
                    sex_ico.setImageResource(R.mipmap.boy);
                }else{
                    sex_ico.setImageResource(R.mipmap.girl);
                }
                //主题
                setSpinnerItemSelectedByValue(u_theme,theme);
                alter_theme=theme;
                if(theme.equals("蓝色")){
                    theme_ico.setImageResource(R.mipmap.theme_blue);
                }else if(theme.equals("紫色")){
                    theme_ico.setImageResource(R.mipmap.themepurple);
                }else if(theme.equals("绿色")){
                    theme_ico.setImageResource(R.mipmap.themegreen);
                }

                //头像

                String headimagename=username+"_headimage.png";
                if (HttpUtils.isExistFile(headimagename,head_dir)){
                    u_headimage.setImageBitmap(HttpUtils.getBitmapFromLocal(headimagename,head_dir));
                }else {
                    downloadImageTask myTask = new downloadImageTask();
                    myTask.execute(headimagename);
                }
            }
            c.close();
        }
    }
//SpinnerItemSelected选中
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }
    //添加控件监听
    public void initEditListener(){
        u_nickname.addTextChangedListener(new EditeListener());
        u_address.addTextChangedListener(new EditeListener());
        u_autopraph.addTextChangedListener(new EditeListener());
    }
    public class EditeListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            alertbtn.setEnabled(true);
            alertbtn.setTextColor(Color.parseColor("#1296db"));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
   //==============clear()
   //====修改存入本地数据库
   public void alterUserAsDB(){

       SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
       String username = sp.getString(USERNAME,null);
       ContentValues value=new ContentValues();
       value.put("nickname", u_nickname.getText().toString());
       value.put("sex", alter_sex);
       value.put("address", u_address.getText().toString());
       value.put("headimage",username+"_headimage");
       value.put("autograph",u_autopraph.getText().toString());
       value.put("theme",alter_theme);
       db.update("user", value, "username='" + username+"'", null);
       SharedPreferences.Editor editor = sp.edit();
       editor.putString(NICKNAME,u_nickname.getText().toString());
       editor.putString(THEME,alter_theme);
       editor.commit();

   }
   //=======获得修改后的User对象
    public String getAlterUser() {
        SharedPreferences sp = getSharedPreferences(SP_INFO, MODE_PRIVATE);
        String username = sp.getString(USERNAME, null);
        String user_json=null;
        if (username != null) {

            Cursor c = db.query("user", new String[]{}, "username='" + username + "'", null, null, null, null);
            if (c.moveToFirst()) {
                String password = c.getString(c.getColumnIndex("password"));
                String address = c.getString(c.getColumnIndex("address"));
                String nickname = c.getString(c.getColumnIndex("nickname"));
                String sex = c.getString(c.getColumnIndex("sex"));
                String autopraph = c.getString(c.getColumnIndex("autograph"));
                String headimage = c.getString(c.getColumnIndex("headimage"));
                String theme = c.getString(c.getColumnIndex("theme"));
                c.close();
                User user = new User(username,password,headimage,nickname,sex,address,autopraph,theme);
                Gson gson = new Gson();
                user_json =gson.toJson(user);
            }
        }
        return user_json;
    }
   //============数据修改上传服务器异步
   class InfoMyTask extends AsyncTask<String,Integer,String> {
       public InfoMyTask(){
           super();
       }
       @Override
       protected void onPostExecute(String s) {
           if(s.equals("修改成功")){
               alertbtn.setTextColor(getResources().getColor(R.color.colorPrimary));
               alertbtn.setEnabled(false);
               Toast.makeText(UserInfo.this,s,Toast.LENGTH_SHORT).show();
           }
       }

       @Override
       protected String doInBackground(String... strings) {
           String param ="user_json="+strings[0];
           return HttpUtils.sendPost(HOST_PATH+"/APPService/alterInfoAction",param);
           //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
       }
   }
//===============================================头像截取
public void gallery() {
    // 激活系统图库，选择一张图片
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
}
    /**
     *把用户选择的图片显示在imageview中
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        }  else if (requestCode == PHOTO_REQUEST_CUT) {
            SharedPreferences sp = getSharedPreferences(SP_INFO, MODE_PRIVATE);
            String username = sp.getString(USERNAME, null);
            // 从剪切图片返回的数据
            if (imageUri != null) {
                Bitmap bitmap = decodeUriBitmap(imageUri);
                HttpUtils.resetpic(username+"_headimage.png",bitmap,head_dir);//修改原来的文件图片
                //====================
                UploadImageTask myTask = new UploadImageTask();
                myTask.execute(UPLOAD_URL,head_dir+username+"_headimage.png");
                //==================
                u_headimage.setImageBitmap(bitmap);
                u_headimage.setScaleType(ImageView.ScaleType.CENTER);
                Toast.makeText(UserInfo.this,imageUri.toString(),Toast.LENGTH_LONG).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private Bitmap decodeUriBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded",true);
        // aspectX aspectY 是宽高的比例
/*        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);*/

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);

        //设置了true的话直接返回bitmap，可能会很占内存
        intent.putExtra("return-data", false);
        //设置输出的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        //设置输出的地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //不启用人脸识别
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //=============================图片下载异步类
    class downloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            return HttpUtils.setPic(params[0],HttpUtils.dowloadImage(HEADIMAGE_URL+params[0]),head_dir);
        }

        // 下载完成回调
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO Auto-generated method stub
            u_headimage.setImageBitmap(bitmap);
        }

    }
    //=============================图片上传异步类
    class UploadImageTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            return HttpUtils.formUpload(params[0],params[1]);
        }

        // 上传回调完成回调
        @Override
        protected void onPostExecute(String s) {
            // TODO Auto-generated method stub
            Toast.makeText(UserInfo.this,s,Toast.LENGTH_SHORT).show();
        }

    }

}
