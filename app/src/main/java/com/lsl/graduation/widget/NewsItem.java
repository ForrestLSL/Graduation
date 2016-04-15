package com.lsl.graduation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.bean.NewModle;
import com.lsl.graduation.net.context.BitmapContext;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * Created by Forrest on 16/4/15.
 */
public class NewsItem extends LinearLayout{
    private LinearLayout mContainer;
    private TextView mTextTitle;
    private TextView mImageTitle;
    private TextView mTextContent;
    private ImageView mImageText;
    private ImageView mImage0;
    private ImageView mImage1;
    private ImageView mImage2;
    private LinearLayout mLinearText;
    private LinearLayout mLinearImage;
    public NewsItem(Context context) {
        super(context);
        initView(context);
    }

    public NewsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NewsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private  void initView(Context context){
        mContainer= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.news_item,null);
        mTextTitle= (TextView) mContainer.findViewById(R.id.item_title);
        mTextContent= (TextView) mContainer.findViewById(R.id.item_content);
        mImageTitle= (TextView) mContainer.findViewById(R.id.item_abstract);
        mImageText= (ImageView) mContainer.findViewById(R.id.right_image);
        mImage0= (ImageView) mContainer.findViewById(R.id.item_image_0);
        mImage1= (ImageView) mContainer.findViewById(R.id.item_image_1);
        mImage2= (ImageView) mContainer.findViewById(R.id.item_image_2);
        mLinearText= (LinearLayout)mContainer.findViewById(R.id.linear_text);
        mLinearImage= (LinearLayout) mContainer.findViewById(R.id.linear_image);
    }
    public void setTexts(Context context,String titleText, String contentText, String imgUrl, String currentItem) {
        mLinearText.setVisibility(View.VISIBLE);
        mLinearImage.setVisibility(View.GONE);
        mTextTitle.setText(titleText);
        if ("北京".equals(currentItem)) {

        } else {
            mTextContent.setText(contentText);
        }
        if (!"".equals(imgUrl)) {
            mImageText.setVisibility(View.VISIBLE);
            new BitmapContext()
                    .get(imgUrl)
                    .ImageView(mImageText)
                    .flag(BitmapContext.FLAG_CACHE_FIRST)
                    .defaultImage(
                            context.getResources().getDrawable(
                                    R.mipmap.default_image)).load();
//            imageLoader.displayImage(imgUrl, leftImage, options);
        } else {
            mImageText.setVisibility(View.GONE);
        }
    }

    public void setImages(Context context,NewModle newModle) {
        mLinearImage.setVisibility(View.VISIBLE);
        mLinearText.setVisibility(View.GONE);
        mImageTitle.setText(newModle.getTitle());
        List<String> imageModle = newModle.getImagesModle().getImgList();
        new BitmapContext()
                .get(imageModle.get(0))
                .ImageView(mImage0)
                .flag(BitmapContext.FLAG_CACHE_FIRST)
                .defaultImage(
                        context.getResources().getDrawable(
                                R.mipmap.default_image)).load();
        new BitmapContext()
                .get(imageModle.get(1))
                .ImageView(mImage1)
                .flag(BitmapContext.FLAG_CACHE_FIRST)
                .defaultImage(
                        context.getResources().getDrawable(
                                R.mipmap.default_image)).load();
        new BitmapContext()
                .get(imageModle.get(2))
                .ImageView(mImage2)
                .flag(BitmapContext.FLAG_CACHE_FIRST)
                .defaultImage(
                        context.getResources().getDrawable(
                                R.mipmap.default_image)).load();
//        imageLoader.displayImage(imageModle.get(0), item_image0, options);
//        imageLoader.displayImage(imageModle.get(1), item_image1, options);
//        imageLoader.displayImage(imageModle.get(2), item_image2, options);
    }
}
