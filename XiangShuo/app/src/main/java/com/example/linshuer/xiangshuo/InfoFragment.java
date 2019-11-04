package com.example.linshuer.xiangshuo;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linshuer.xiangshuo.bean.Article;
import com.example.linshuer.xiangshuo.bean.User;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {
    private SwipeRefreshLayout swiper;
    private ListView p_listview;
    private TextView setting,info_xx,info_yy,info_count;
    private ImageView headimage;
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String NICKNAME="unickname";
    private static String heaa_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiangshuo/headimage/";
    private final String HOST_PATH="http://192.168.0.111:8080";
    private int count=0;
    private ArticleListAdpter adapter;
    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        p_listview=(ListView)getActivity().findViewById(R.id.p_listview);
        setting =(TextView) getActivity().findViewById(R.id.setting);
        info_xx =(TextView) getActivity().findViewById(R.id.info_xx);
        info_yy =(TextView) getActivity().findViewById(R.id.info_yy);
        info_count=(TextView)getActivity().findViewById(R.id.count);
        headimage=(ImageView)getActivity().findViewById(R.id.headimage);
        swiper = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeLayout2);
        swiper.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple);
        //为SwipeRefreshLayout设置监听事件
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
                        String username = sp.getString(USERNAME,null);
                        MyTask myTask = new MyTask();
                        myTask.execute(username);
                        //结束后停止刷新
                        swiper.setRefreshing(false);
                    }
                }, 3000);

            }
        });
        p_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String articleid= (String) ((TextView)view.findViewById(R.id.v_id)).getText();
                Intent intent=new Intent(getActivity(),ArticleInfo.class);
                intent.putExtra("articleid",articleid);
                startActivity(intent);
            }
        });
        p_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String articleid= (String) ((TextView)view.findViewById(R.id.v_id)).getText();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(android.R.drawable.ic_dialog_info);
               /* builder.setTitle("温馨提示");*/
                builder.setMessage("是否删除？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteMyTask deleteMyTask = new DeleteMyTask();
                        deleteMyTask.execute(articleid);
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
                return true;
            }
        });
        setheadimage();
        setViewListener();
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        String username = sp.getString(USERNAME, null);
        String inckname = sp.getString(NICKNAME,null);
        info_xx.setText(inckname);
        info_yy.setText(username);
        MyTask myTask = new MyTask();
        myTask.execute(username);
    }


    //======设置头像
    public void setheadimage(){
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        String username = sp.getString(USERNAME,null);
        String headimagename=username+"_headimage.png";
        if (HttpUtils.isExistFile(headimagename,heaa_dir)){
            headimage.setImageBitmap(HttpUtils.getBitmapFromLocal(headimagename,heaa_dir));
        }
    }
    //==============监听
    public void setViewListener(){
        setting.setOnClickListener(onClickListener);
        info_xx.setOnClickListener(onClickListener);
        info_yy.setOnClickListener(onClickListener);
        headimage.setOnClickListener(onClickListener);

    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),UserInfo.class);
            startActivity(intent);
            }

    };

    //=======数据列表加载异步类
    class MyTask extends AsyncTask<String,Integer,String> {
        public MyTask(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("outtime")){
                Toast.makeText(getActivity(),"网络超时",Toast.LENGTH_SHORT).show();
            }else if(s!=null&&!s.equals("error")){
                Gson gson = new Gson();
                Log.i("cccccc",s);
                List<Article> all =gson.fromJson(s,new TypeToken<List<Article>>(){}.getType());
                Collections.reverse(all);
                count =all.size();
                info_count.setText(String.valueOf(count));
                adapter =new ArticleListAdpter(getActivity(),all);
                p_listview.setAdapter(adapter);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="master="+strings[0];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/myArticleAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }
    //=======数据删除加载异步类
    class DeleteMyTask extends AsyncTask<String,Integer,String> {
        public DeleteMyTask(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("outtime")){
                Toast.makeText(getActivity(),"网络超时",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="articleid="+strings[0];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/deleteArticleAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }

}
