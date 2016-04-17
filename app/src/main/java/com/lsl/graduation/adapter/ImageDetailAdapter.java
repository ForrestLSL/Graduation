package com.lsl.graduation.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.R;
import com.lsl.graduation.net.context.BitmapContext;
import com.lsl.graduation.utils.UIHelper;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Forrest on 16/4/17.
 */
public class ImageDetailAdapter extends PagerAdapter{
    private List<String> imgList;
    private Context context;

    public ImageDetailAdapter(List<String> imgList, Context context) {
        this.imgList = imgList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgList==null?0:imgList.size();
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
        View item= LayoutInflater.from(context).inflate(R.layout.image_item,null);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, AppContext.deviceWidth*11/10);
        PhotoView img= (PhotoView) item.findViewById(R.id.current_image);
        img.setLayoutParams(params);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);
        TextView text= (TextView) item.findViewById(R.id.current_page);
        new BitmapContext()
                .get(imgList.get(position))
                .ImageView(img)
                .flag(BitmapContext.FLAG_CACHE_FIRST)
                .defaultImage(context.getResources().getDrawable(R.mipmap.default_image)).load();
        text.setText((position + 1) + "/" + imgList.size());

        img.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float v, float v1) {
//                UIHelper.showMsg(context,"点击了图片");
            }
        });
        container.addView(item);
        return item;
    }
}
