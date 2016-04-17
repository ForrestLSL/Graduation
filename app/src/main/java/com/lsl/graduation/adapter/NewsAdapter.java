package com.lsl.graduation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.activity.ImageDetailActivity;
import com.lsl.graduation.activity.NewsDetailActivity;
import com.lsl.graduation.bean.NewModle;
import com.lsl.graduation.net.context.BitmapContext;
import com.lsl.graduation.utils.UIHelper;
import com.lsl.graduation.widget.NewsItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/15.
 */
public class NewsAdapter extends BaseAdapter{
    public List<NewModle> lists = new ArrayList<NewModle>();
    private Context context;
    private String currentItem;

    public void appendList(List<NewModle> list) {
        if (!lists.containsAll(list) && list != null && list.size() > 0) {
            lists.addAll(list);
        }
        notifyDataSetChanged();
    }

    public NewsAdapter(Context context,List<NewModle> data){
        this.lists=data;
        this.context=context;
    }
    public void clear() {
        lists.clear();
        notifyDataSetChanged();
    }

    public void currentItem(String item) {
        this.currentItem = item;
    }

    @Override
    public int getCount() {
        return lists==null?0:lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
//        NewsItem newsItem;
//        if (convertView==null){
//            newsItem=new NewsItem(context);
//
//        }else {
//            newsItem= (NewsItem) convertView;
//        }
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.news_item,null);
            holder=new ViewHolder();
            convertView.setTag(holder);
            holder.mTextTitle= (TextView) convertView.findViewById(R.id.item_title);
            holder.mTextContent= (TextView) convertView.findViewById(R.id.item_content);
            holder.mImageTitle= (TextView) convertView.findViewById(R.id.item_abstract);
            holder.mImageText= (ImageView) convertView.findViewById(R.id.right_image);
            holder.mImage0= (ImageView) convertView.findViewById(R.id.item_image_0);
            holder.mImage1= (ImageView) convertView.findViewById(R.id.item_image_1);
            holder.mImage2= (ImageView) convertView.findViewById(R.id.item_image_2);
            holder.mLinearText= (LinearLayout) convertView.findViewById(R.id.linear_text);
            holder.mLinearImage= (LinearLayout) convertView.findViewById(R.id.linear_image);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        final NewModle newModle = lists.get(position);
        if (newModle.getImagesModle() == null){
            holder.mLinearText.setVisibility(View.VISIBLE);
            holder.mLinearImage.setVisibility(View.GONE);
            holder. mTextTitle.setText(newModle.getTitle());
            if ("北京".equals(currentItem)) {

            } else {
                holder.mTextContent.setText(newModle.getDigest());
            }
            if (!"".equals(newModle.getImgsrc())) {
                holder.mImageText.setVisibility(View.VISIBLE);
                new BitmapContext()
                        .get(newModle.getImgsrc())
                        .ImageView(holder.mImageText)
                        .flag(BitmapContext.FLAG_CACHE_FIRST)
                        .defaultImage(
                                context.getResources().getDrawable(
                                        R.mipmap.default_image)).load();
            } else {
                holder. mImageText.setVisibility(View.GONE);
            }
        }else {
            holder.mLinearImage.setVisibility(View.VISIBLE);
            holder.mLinearText.setVisibility(View.GONE);
            holder.mImageTitle.setText(newModle.getTitle());
            List<String> imageModle = newModle.getImagesModle().getImgList();
            new BitmapContext()
                    .get(imageModle.get(0))
                    .ImageView( holder.mImage0)
                    .flag(BitmapContext.FLAG_CACHE_FIRST)
                    .defaultImage(context.getResources().getDrawable(R.mipmap.default_image)).load();
            new BitmapContext()
                    .get(imageModle.get(1))
                    .ImageView( holder.mImage1)
                    .flag(BitmapContext.FLAG_CACHE_FIRST)
                    .defaultImage(context.getResources().getDrawable(R.mipmap.default_image)).load();
            new BitmapContext()
                    .get(imageModle.get(2))
                    .ImageView( holder.mImage2)
                    .flag(BitmapContext.FLAG_CACHE_FIRST)
                    .defaultImage(context.getResources().getDrawable(R.mipmap.default_image)).load();
        }

//
//        if (newModle.getImagesModle() == null) {
//            newsItem.setTexts(context,newModle.getTitle(), newModle.getDigest(),
//                    newModle.getImgsrc(), currentItem);
//        } else {
//            newsItem.setImages(context,newModle);
//        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("newModle", newModle);
                Class<?> class1 = null;
                if (newModle.getImagesModle() != null && newModle.getImagesModle().getImgList().size() > 1) {
                    class1 = ImageDetailActivity.class;
                } else {
                    class1 = NewsDetailActivity.class;
                }
                intent.setClass(context,class1);
               context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView mTextTitle;
        TextView mImageTitle;
        TextView mTextContent;
        ImageView mImageText;
        ImageView mImage0;
        ImageView mImage1;
        ImageView mImage2;
        LinearLayout mLinearText;
        LinearLayout mLinearImage;
    }
}
