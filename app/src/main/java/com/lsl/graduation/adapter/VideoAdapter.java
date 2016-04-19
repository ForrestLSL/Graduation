package com.lsl.graduation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.R;
import com.lsl.graduation.activity.VideoPlayActivity;
import com.lsl.graduation.bean.VideoModel;
import com.lsl.graduation.net.context.BitmapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/19.
 */
public class VideoAdapter extends BaseAdapter{
    private Context context;
    private List<VideoModel> data=new ArrayList<>();

    public VideoAdapter(Context context, List<VideoModel> data) {
        this.context = context;
        this.data = data;
    }

    public void appendList(List<VideoModel> list) {
        if (!data.containsAll(list) && list != null && list.size() > 0) {
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data==null?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_video,null);
            holder=new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.video_img);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, AppContext.deviceWidth*6/10);
            holder.img.setLayoutParams(params);
            holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.title= (TextView) convertView.findViewById(R.id.video_title);
            holder.time= (TextView) convertView.findViewById(R.id.video_time);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        final VideoModel bean= data.get(position);
        new BitmapContext().get(bean.getCover())
                .defaultImage(context.getResources().getDrawable(R.mipmap.default_image))
                .ImageView(holder.img)
                .flag(BitmapContext.FLAG_CACHE_FIRST)
                .load();
        holder.title.setText(bean.getTitle());
        holder.time.setText(bean.getLength());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context, VideoPlayActivity.class);
                intent.putExtra("playUrl", bean.getMp4Hd_url());
                intent.putExtra("filename", bean.getTitle());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView img;
        TextView title;
        TextView time;

    }
}
