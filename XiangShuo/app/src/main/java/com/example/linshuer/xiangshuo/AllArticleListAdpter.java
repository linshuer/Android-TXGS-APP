package com.example.linshuer.xiangshuo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.linshuer.xiangshuo.bean.Article;
import com.example.linshuer.xiangshuo.httpcon.HttpUtils;

import java.util.List;

/**
 * Created by Linshuer on 2018/6/15.
 */

public class AllArticleListAdpter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Article> list;
    private ListView listview;
    private Context context;
    ArticleListAdpter.ViewHolder viewHolder =null;
    private final String PICIMAGE_URL="http://192.168.0.111:8080/APPService/picimage/";
    private LruCache<String, BitmapDrawable> mImageCache;
    public AllArticleListAdpter(Context context, List<Article> list) {
        inflater = LayoutInflater.from(context);
        this.list =list;
        this.context =context;
        ///===============================================
        int maxCache = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxCache / 8;
        mImageCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable value) {
                return value.getBitmap().getByteCount();
            }
        };
        //============================================
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (listview == null) {
            listview = (ListView) viewGroup;
        }
        if (view==null){
            view =inflater.inflate(R.layout.list_item_home,null);
            viewHolder = new ArticleListAdpter.ViewHolder();
            viewHolder.articleid=(TextView)view.findViewById(R.id.h_id);
            viewHolder.date =(TextView)view.findViewById(R.id.home_date);
            viewHolder.type_inckname =(TextView)view.findViewById(R.id.home_type_nickname);
            viewHolder.title =(TextView)view.findViewById(R.id.home_title);
            viewHolder.p_pic =(ImageView)view.findViewById(R.id.h_image);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ArticleListAdpter.ViewHolder)view.getTag();
        }
        viewHolder.articleid.setText(String.valueOf(list.get(i).getArticleid()));
        viewHolder.date.setText(list.get(i).getDate());
        viewHolder.type_inckname.setText(list.get(i).getType()+" | "+list.get(i).getMnickname());
        viewHolder.title.setText(list.get(i).getTitle());
        viewHolder.p_pic.setTag(list.get(i).getPicimage());
        if (mImageCache.get(list.get(i).getPicimage()) != null) {
            viewHolder.p_pic.setImageDrawable(mImageCache.get(list.get(i).getPicimage()));
        } else {
            downloadImageTask it = new downloadImageTask();
            it.execute(list.get(i).getPicimage());
        }
        return view;
    }
    public static class ViewHolder{
        TextView date,type_inckname,title,articleid;
        ImageView p_pic;
    }
    //=============================图片下载异步类
    class downloadImageTask extends AsyncTask<String, Void, BitmapDrawable> {
        private String imageUrl;
        @Override
        protected BitmapDrawable  doInBackground(String... params) {
            imageUrl =params[0];
            Bitmap bitmap = HttpUtils.dowloadImage(PICIMAGE_URL+imageUrl);;
            BitmapDrawable db = new BitmapDrawable(listview.getResources(),
                    bitmap);
            // 如果本地还没缓存该图片，就缓存
            if (mImageCache.get(imageUrl) == null) {
                mImageCache.put(imageUrl, db);
            }
            return db;
        }

        // 下载完成回调
        @Override
        protected void onPostExecute(BitmapDrawable result) {
            ImageView iv = (ImageView) listview.findViewWithTag(imageUrl);
            if (iv != null && result != null) {
                iv.setImageDrawable(result);
            }
        }

    }
}
