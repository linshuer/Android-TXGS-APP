package com.example.linshuer.xiangshuo;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linshuer.xiangshuo.bean.Article;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    private ImageView image;
    private TextView send,today,c_nickname;
    private EditText title,content;
    private Spinner c_type;
    private final int PHOTO_REQUEST_GALLERY=1;
    private final int PHOTO_REQUEST_CUT=2;
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String NICKNAME="unickname";
    public Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath()+"/"+"temppic.png"));
    private final String PICIMAGE_URL="http://192.168.0.111:8080/APPService/picimage/";
    private final String HOST_PATH="http://192.168.0.111:8080";
    public static final String UPLOAD_URL="http://192.168.0.111:8080/APPService/uploadPicAction";
    private static String pic_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiangshuo/picimage/";
    private String type="原创";
    private String Crop_name="";
    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        send=(TextView)getActivity().findViewById(R.id.send);
        image =(ImageView)getActivity().findViewById(R.id.image);
        title =(EditText)getActivity().findViewById(R.id.title);
        content=(EditText)getActivity().findViewById(R.id.content);
        c_type =(Spinner)getActivity().findViewById(R.id.c_type);
        c_nickname=(TextView)getActivity().findViewById(R.id.c_nickname);
        today=(TextView)getActivity().findViewById(R.id.today);
        //================
        today.setText(getTodayDate());
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        String nickname = sp.getString(NICKNAME, null);
        c_nickname.setText("|  "+nickname);
        //===================
        c_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallery();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(title.getText().toString()!=null&&content.getText().toString()!=null){

                if(!title.getText().toString().isEmpty()&&!content.getText().toString().isEmpty()){
                    SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
                    String username = sp.getString(USERNAME, null);
                    String nickname = sp.getString(NICKNAME, null);
                    String date=getTodayDate();
                    String pic_name =null;
                    if (Crop_name!=""){
                        pic_name =Crop_name;
                        UploadImageTask myTask = new UploadImageTask();
                        myTask.execute(UPLOAD_URL,pic_dir+pic_name);
                        Crop_name="";
                    }else{
                        pic_name="demo_picimage.png";
                    }
                    String master =username;
                    String mnickname =nickname;
                    String c_title =title.getText().toString();
                    String c_content =content.getText().toString();
                    Article article =new Article(c_title,type,pic_name,master,mnickname,date,c_content);
                    Gson gson = new Gson();
                    String article_json =gson.toJson(article);
                    InfoMyTask infoMyTask = new InfoMyTask();
                    infoMyTask.execute(article_json);
                }else{
                    Toast.makeText(getActivity(),"请先填写标题或内容",Toast.LENGTH_SHORT).show();
                }

                }else {
                    Toast.makeText(getActivity(),"请先填写标题或内容",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
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
            // 从剪切图片返回的数据
            if (imageUri != null) {
                Bitmap bitmap = decodeUriBitmap(imageUri);
                //===============
                Crop_name =getPicName();
                HttpUtils.resetpic(Crop_name,bitmap,pic_dir);
                //===============
                image.setImageBitmap(bitmap);
                //Toast.makeText(getActivity(),imageUri.toString(),Toast.LENGTH_LONG).show();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private Bitmap decodeUriBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
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
    //获得一个图片名
    public String getPicName(){
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        String username = sp.getString(USERNAME, null);
        final Calendar calendar = Calendar.getInstance();
        final int now_year = calendar.get(Calendar.YEAR);
        final int now_month = calendar.get(Calendar.MONTH)+1;
        final int now_day = calendar.get(Calendar.DAY_OF_MONTH);
        String pic_name =username+now_year+now_month+now_day+calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)+".png";
        return pic_name;
    }
    //获得一个r日期
    public String getTodayDate(){

        final Calendar calendar = Calendar.getInstance();
        final int now_year = calendar.get(Calendar.YEAR);
        final int now_month = calendar.get(Calendar.MONTH)+1;
        final int now_day = calendar.get(Calendar.DAY_OF_MONTH);
        String date=now_year+"/"+now_month+"/"+now_day;
        return date;
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
        }

    }

    //=============================信息上传异步类
    class InfoMyTask extends AsyncTask<String,Integer,String> {
        public InfoMyTask() {
            super();
        }

        @Override
        protected void onPostExecute(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String param = "article_json=" + strings[0];
            return HttpUtils.sendPost(HOST_PATH + "/APPService/addArticleAction", param);
        }
    }
}
