package com.lsl.graduation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.activity.PictureDetailActivity;
import com.lsl.graduation.bean.PictureModel;
import com.lsl.graduation.net.context.BitmapContext;

import java.util.List;

/**
 * Created by Forrest on 16/4/19.
 */
public class PictureAdapter extends BaseAdapter {
    private Context context;
    private List<PictureModel> data;

    public PictureAdapter(Context context, List<PictureModel> data) {
        this.context = context;
        this.data = data;
    }
    public void appendList(List<PictureModel> list) {
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
        return data!=null?data.size():0;
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
            convertView= LayoutInflater.from(context).inflate(R.layout.item_photo,null);
            holder=new ViewHolder();
            holder.imageView= (ImageView) convertView.findViewById(R.id.photo_img);
            holder.textView= (TextView) convertView.findViewById(R.id.photo_text);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        final PictureModel bean=data.get(position);
        new BitmapContext()
                .get(bean.getPic())
                .defaultImage(context.getResources().getDrawable(R.mipmap.default_image))
                .ImageView(holder.imageView)
                .load();
        holder.textView.setText(bean.getTitle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context, PictureDetailActivity.class);
                intent.putExtra("pic_id", bean.getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
