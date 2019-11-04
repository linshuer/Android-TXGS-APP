package com.example.linshuer.xiangshuo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linshuer.xiangshuo.httpcon.HttpUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String SP_INFO="login";
    private final static String USERNAME="uname";
    private final static String PASSWORD="upass";
    private final static String THEME="utheme";
    //声明ViewPager
    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;

    //三个Tab对应的布局
    private LinearLayout mTabEdit;
    private LinearLayout mTabHome;
    private LinearLayout mTabInfo;

    //三个Tab对应的ImageButton
    private ImageButton mImgEditt;
    private ImageButton mImgHome;
    private ImageButton mImgInfo;
    //
    private TextView du;
    private TextView xie;
    private TextView wo;
    //
    //private static String pic_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiangshuo/picimage/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/*        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);*/
        SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        String theme = sp.getString(THEME,null);
        initViews();//初始化控件
        intTheme(theme);//初始home
        initEvents();//初始化事件
        initDatas();//初始化数据
        //HttpUtils.makeDir(pic_dir);//创建目录
    }
    public void initDatas() {
        mFragments = new ArrayList<>();
        //将四个Fragment加入集合中
        mFragments.add(new HomeFragment());
        mFragments.add(new EditFragment());
        mFragments.add(new InfoFragment());

        //初始化适配器
        mAdapter = new FragmentAdapter(
                getSupportFragmentManager());
        mViewPager.setOffscreenPageLimit(3);
        mAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            //页面滚动事件
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面选中事件
            @Override
            public void onPageSelected(int position) {
                //设置position对应的集合中的Fragment
                //mViewPager.setCurrentItem(position);
                resetImgs();
                SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
                String theme = sp.getString(THEME,null);
                selectTab(position,theme);
            }

            @Override
            //页面滚动状态改变事件
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //==================
    private void initEvents() {
        //设置四个Tab的点击事件
        mTabHome.setOnClickListener(onClickListener);
        mTabEdit.setOnClickListener(onClickListener);
        mTabInfo.setOnClickListener(onClickListener);


    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //先将四个ImageButton置为灰色
            resetImgs();
            SharedPreferences sp = getSharedPreferences(SP_INFO,MODE_PRIVATE);
            String theme = sp.getString(THEME,null);
            //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
            switch (v.getId()) {
                case R.id.id_tab_home:
                    selectTab(0,theme);
                    break;
                case R.id.id_tab_edit:
                    selectTab(1,theme);
                    break;
                case R.id.id_tab_info:
                    selectTab(2,theme);
                    break;
            }
        }
    };
    //=============================
    //初始化控件
    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        mTabEdit = (LinearLayout) findViewById(R.id.id_tab_edit);
        mTabHome = (LinearLayout) findViewById(R.id.id_tab_home);
        mTabInfo = (LinearLayout) findViewById(R.id.id_tab_info);

        mImgEditt = (ImageButton) findViewById(R.id.id_tab_edit_img);
        mImgHome = (ImageButton) findViewById(R.id.id_tab_home_img);
        mImgInfo = (ImageButton) findViewById(R.id.id_tab_info_img);

        du =(TextView) findViewById(R.id.du);
        xie =(TextView) findViewById(R.id.xie);
        wo =(TextView) findViewById(R.id.wo);
    }
    //==================================
    private void selectTab(int i,String theme){//主题选色
        if(theme.equals("蓝色")){
            selectTabBlue(i);
        }else if(theme.equals("紫色")){
            selectTabPurple(i);
        }else if(theme.equals("绿色")){
            selectTabGreen(i);
        }else{
            selectTabBlue(i);
        }
    }
    //==============================
    private void selectTabBlue(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgHome.setImageResource(R.mipmap.onhome);
                du.setTextColor(getResources().getColor(R.color.themeblue));
                break;
            case 1:
                mImgEditt.setImageResource(R.mipmap.onedit);
                xie.setTextColor(getResources().getColor(R.color.themeblue));
                break;
            case 2:
                mImgInfo.setImageResource(R.mipmap.oninfo);
                wo.setTextColor(getResources().getColor(R.color.themeblue));
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }
    private void selectTabPurple(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgHome.setImageResource(R.mipmap.homepurple);
                du.setTextColor(getResources().getColor(R.color.themepurple));
                break;
            case 1:
                mImgEditt.setImageResource(R.mipmap.editpurple);
                xie.setTextColor(getResources().getColor(R.color.themepurple));
                break;
            case 2:
                mImgInfo.setImageResource(R.mipmap.infopurple);
                wo.setTextColor(getResources().getColor(R.color.themepurple));
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }
    private void selectTabGreen(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgHome.setImageResource(R.mipmap.homegreen);
                du.setTextColor(getResources().getColor(R.color.themegreen));
                break;
            case 1:
                mImgEditt.setImageResource(R.mipmap.editgreen);
                xie.setTextColor(getResources().getColor(R.color.themegreen));
                break;
            case 2:
                mImgInfo.setImageResource(R.mipmap.infogreen);
                wo.setTextColor(getResources().getColor(R.color.themegreen));
                break;
        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }
    //===================================
    //将四个ImageButton设置为灰色
    private void resetImgs() {
        mImgEditt.setImageResource(R.mipmap.edit);
        xie.setTextColor(Color.parseColor("#000000"));
        mImgHome.setImageResource(R.mipmap.home);
        du.setTextColor(Color.parseColor("#000000"));
        mImgInfo.setImageResource(R.mipmap.info);
        wo.setTextColor(Color.parseColor("#000000"));
    }
    //s设置home
    private void intTheme(String theme){
        if(theme.equals("蓝色")){
            mImgHome.setImageResource(R.mipmap.onhome);
            du.setTextColor(getResources().getColor(R.color.themeblue));
        }else if(theme.equals("紫色")){
            mImgHome.setImageResource(R.mipmap.homepurple);
            du.setTextColor(getResources().getColor(R.color.themepurple));
        }else if(theme.equals("绿色")){
            mImgHome.setImageResource(R.mipmap.homegreen);
            du.setTextColor(getResources().getColor(R.color.themegreen));
        }
    }
    ///=============


}
