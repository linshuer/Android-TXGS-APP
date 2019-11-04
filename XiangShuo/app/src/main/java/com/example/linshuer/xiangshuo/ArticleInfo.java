package com.example.linshuer.xiangshuo;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linshuer.xiangshuo.bean.Article;
import com.example.linshuer.xiangshuo.bean.User;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;
import com.google.gson.Gson;

import java.io.File;

public class ArticleInfo extends AppCompatActivity {
    private TextView a_title,a_type_username,a_date_count,a_conten;
    private ImageView a_image,back,share;
    private final String HOST_PATH="http://192.168.0.111:8080";
    private final String PICIMAGE_URL="http://192.168.0.111:8080/APPService/picimage/";
    String imagename =null;
    LinearLayout scrolllayout;
    //=================
    private final String WX_PKG ="com.tencent.mm";
    private final String QQ_PKG ="com.tencent.mobileqq";
    private final String WX_CLS_PY ="com.tencent.mm.ui.tools.ShareToTimeLineUI";
    private final String WX_CLS_WX ="com.tencent.mm.ui.tools.ShareImgUI";
    private final String WX_CLS_QQ ="com.tencent.mobileqq.activity.JumpActivity";
    //===================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_info);
        a_title =(TextView)findViewById(R.id.a_title);
        a_type_username =(TextView)findViewById(R.id.a_type_username);
        a_date_count =(TextView)findViewById(R.id.a_date_count);
        a_conten =(TextView)findViewById(R.id.a_conten);
        a_image=(ImageView)findViewById(R.id.a_image);
        back=(ImageView)findViewById(R.id.back);
        share=(ImageView)findViewById(R.id.share);
        scrolllayout =(LinearLayout)findViewById(R.id.share_layout);
        //===
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showShareDialog();
            }
        });
        //====
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleInfo.this.finish();
            }
        });
        if(getIntent().getStringExtra("articleid")!=null)//判断是新增还是修改
        {
            String id=getIntent().getStringExtra("articleid");
            MyTask myTask = new MyTask();
            myTask.execute(id);
        }else{
            Toast.makeText(this,"不存在",Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void initArticle(String userJson){
        Gson gson = new Gson();
        Article article = gson.fromJson(userJson,Article.class);
        a_title.setText(article.getTitle());
        a_conten.setText(article.getContent());
        a_date_count.setText(article.getDate());
        a_type_username.setText(article.getType()+" | "+article.getMnickname());
        String imagename =article.getPicimage();
        downloadImageTask myTask = new downloadImageTask();
        myTask.execute(imagename);
    }
//========================share

    private void showShareDialog() {

        View view = LayoutInflater.from(ArticleInfo.this).inflate(R.layout.sharedialog, null);
        // 设置style 控制默认dialog带来的边距问题
        final Dialog dialog = new Dialog(this,R.style.common_dialog);
        dialog.setContentView(view);
        dialog.show();

        // 监听
        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.view_share_weixin:
                        // 分享到微信
                        Intent shareIntent = shareImage(scrolllayout,WX_PKG,WX_CLS_WX);
                        startActivity(shareIntent);
                        break;
                    case R.id.view_share_pengyou:
                        // 分享到朋友圈
                        Intent shareIntent1 = shareImage(scrolllayout,WX_PKG,WX_CLS_PY);
                        startActivity(shareIntent1);
                        break;
                    case R.id.view_share_qq:
                        // 分享到qq
                        Intent shareIntent2 = shareImage(scrolllayout,QQ_PKG,WX_CLS_QQ);
                        startActivity(shareIntent2);
                        break;
                    case R.id.share_cancel_btn:
                        // 取消
                        break;

                }

                dialog.dismiss();
            }

        };
        ViewGroup mViewWeixin = (ViewGroup) view.findViewById(R.id.view_share_weixin);
        ViewGroup mViewPengyou = (ViewGroup) view.findViewById(R.id.view_share_pengyou);
        ViewGroup mViewQQ = (ViewGroup) view.findViewById(R.id.view_share_qq);
        Button mBtnCancel = (Button) view.findViewById(R.id.share_cancel_btn);
        mViewWeixin.setOnClickListener(listener);
        mViewPengyou.setOnClickListener(listener);
        mViewQQ.setOnClickListener(listener);
        mBtnCancel.setOnClickListener(listener);

        // 设置相关位置，一定要在 show()之后
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

    }
    ///================================
    public static Intent shareImage(View headerView,String comppkg,String compcls){
        Bitmap bitmap =ShareAction.getBitmapByView(headerView);
        bitmap =ShareAction.compressImage(bitmap);
        Intent shareIntent = new Intent();
        File file = ShareAction.bitMap2File(bitmap);
        if (file != null && file.exists() && file.isFile()) {
            Uri imageUri = Uri.fromFile(file);
            ComponentName comp = new ComponentName(comppkg, compcls);
            shareIntent.setComponent(comp);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
        }
        return shareIntent;
    }


    //============================
    class MyTask extends AsyncTask<String,Integer,String> {
        public MyTask(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("error")||s==null){//保存到数据库
                Toast.makeText(ArticleInfo.this, "密码或账号错误", Toast.LENGTH_SHORT).show();
            }else if(s.equals("outtime")){
                Toast.makeText(ArticleInfo.this,"网络超时",Toast.LENGTH_SHORT).show();
            }else {
                initArticle(s);//=======================================
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="articleid="+strings[0];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/articleInfoShowAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }
    class downloadImageTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {

            return HttpUtils.dowloadImage(PICIMAGE_URL+params[0]);
        }

        // 下载完成回调
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            // TODO Auto-generated method stub
            a_image.setImageBitmap(bitmap);
        }

    }
}
