package com.lsl.graduation.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.R;
import com.lsl.graduation.bean.PictureDetailModel;
import com.lsl.graduation.net.context.BitmapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/19.
 */
public class PictureDetailAdapter extends PagerAdapter{
    public List<PictureDetailModel> data = new ArrayList<PictureDetailModel>();
    private Context context;

    public PictureDetailAdapter(Context context, List<PictureDetailModel> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PictureDetailModel bean=data.get(position);
        View item= LayoutInflater.from(context).inflate(R.layout.picture_item, null);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, AppContext.deviceWidth*11/10);
        //图片
        ImageView imageView= (ImageView) item.findViewById(R.id.current_image);
        imageView.setLayoutParams(params);
        String imgurl=bean.getPic();
        imgurl.replace("auto", "854x480x75x0x0x3");
        new BitmapContext()
                .get(imgurl)
                .ImageView(imageView)
                .flag(BitmapContext.FLAG_CACHE_FIRST)
                .defaultImage(context.getResources().getDrawable(R.mipmap.default_image)).load();
        //数量
        TextView photo_count= (TextView) item.findViewById(R.id.photo_count);
        photo_count.setText((position + 1) + "/" + data.size());
        //内容
        TextView photo_content=(TextView) item.findViewById(R.id.photo_content);
        photo_content.setText(bean.getAlt());
        //标题
        TextView photo_title=(TextView) item.findViewById(R.id.photo_title);
        photo_title.setText(bean.getTitle());

        container.addView(item);
        return item;

    }
}
