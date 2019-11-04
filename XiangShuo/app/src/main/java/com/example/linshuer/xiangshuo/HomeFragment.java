package com.example.linshuer.xiangshuo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linshuer.xiangshuo.bean.Article;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private SwipeRefreshLayout swiper;
    private LoadMoreListView mListView;
    private final String HOST_PATH="http://192.168.0.111:8080";
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String P_PAGE="ppage";
    private final static String D_PAGE="dpage";
    private final static String INITLIST="ulist";
    private AllArticleListAdpter adapter;
    private int page=1;
    private int d_page=1;
    List<Article> all= new ArrayList<Article>();
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swiper = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeLayout);
        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swiper.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_purple);
        //初始化ListView
        mListView = (LoadMoreListView) getActivity().findViewById(R.id.home_listview);
        mListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {
                loadMore();
            }
        });
        //=========================
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        page =sp.getInt(P_PAGE,0);
        page+=1;
        MyTask initMyTask = new MyTask();
        initMyTask.execute(String.valueOf(page));

        //为SwipeRefreshLayout设置监听事件
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
                        page =sp.getInt(P_PAGE,0);
                        page+=1;
                        MyTask myTask = new MyTask();
                        myTask.execute(String.valueOf(page));
                        //结束后停止刷新
                        swiper.setRefreshing(false);
                    }
                }, 3000);

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String articleid= (String) ((TextView)view.findViewById(R.id.h_id)).getText();
                Intent intent=new Intent(getActivity(),ArticleInfo.class);
                intent.putExtra("articleid",articleid);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        page =sp.getInt(P_PAGE,2);
        editor.putInt(P_PAGE, page-1);
        editor.putInt(D_PAGE,page);
        editor.commit();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        page =sp.getInt(P_PAGE,2);
        if(page==1){
            page+=1;
        }
        editor.putInt(P_PAGE, page-1);
        editor.putInt(D_PAGE,page);
        editor.commit();
        super.onStop();
    }
///===============================
private void loadMore() {
    new Thread(){
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
            d_page =sp.getInt(D_PAGE,1);
            d_page-=1;
            MyTask2 myTask2 = new MyTask2();
            myTask2.execute(String.valueOf(d_page));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    mListView.setLoadCompleted();
                }
            });
        }
    }.start();
}
    //=======数据列表加载异步类------下拉
    class MyTask extends AsyncTask<String,Integer,String> {
        public MyTask(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("outtime")){
                Toast.makeText(getActivity(),"网络超时",Toast.LENGTH_SHORT).show();
            }else if(s==null&&s.equals("error")){
                SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
                Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
            }else if(s.equals("已无更多数据")){
                Toast.makeText(getActivity(),"已无更多数据",Toast.LENGTH_SHORT).show();
            }else{
                Gson gson = new Gson();
                List<Article> list =gson.fromJson(s,new TypeToken<List<Article>>(){}.getType());
                list.addAll(all);
                adapter =new AllArticleListAdpter(getActivity(),list);
                mListView.setAdapter(adapter);
                all=list;
                SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(P_PAGE, page);
                editor.commit();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="showpage="+strings[0];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/allArticleAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }
    //=======数据列表加载异步类--------上拉
    class MyTask2 extends AsyncTask<String,Integer,String> {
        public MyTask2(){
            super();
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("outtime")){
                Toast.makeText(getActivity(),"网络超时",Toast.LENGTH_SHORT).show();
            }else if(s==null&&s.equals("error")){
                Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
            }else if(s.equals("已无更多数据")){
                Toast.makeText(getActivity(),"已无更多数据",Toast.LENGTH_SHORT).show();
            }else{
                Gson gson = new Gson();
                List<Article> list =gson.fromJson(s,new TypeToken<List<Article>>(){}.getType());
                all.addAll(list);
                adapter =new AllArticleListAdpter(getActivity(),all);
                mListView.setAdapter(adapter);
                SharedPreferences sp = getActivity().getSharedPreferences(SP_INFO, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(D_PAGE, d_page);
                editor.commit();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String param ="showpage="+strings[0];
            return HttpUtils.sendPost(HOST_PATH+"/APPService/allArticleAction",param);
            //http://192.168.0.111:8080/AndroidXX/loginServlet(连同一个WiFi时为111，电脑连线时为109)
        }
    }
}
