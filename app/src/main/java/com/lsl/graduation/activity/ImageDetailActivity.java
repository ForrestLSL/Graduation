package com.lsl.graduation.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.adapter.ImageDetailAdapter;
import com.lsl.graduation.bean.NewDetailModle;
import com.lsl.graduation.bean.NewModle;

import java.util.List;

/**
 * Created by Forrest on 16/4/16.
 */
public class ImageDetailActivity extends BaseActivity implements View.OnClickListener{
    private TextView new_title;
    private ViewPager image_viewpager;
    private List<String> imgList;
    private NewDetailModle newDetailModle;
    private String titleString;
    private NewModle newModle;
    private ImageDetailAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        new_title= (TextView) findViewById(R.id.new_title);
        image_viewpager= (ViewPager) findViewById(R.id.image_viewpager);
        findViewById(R.id.linear_left).setOnClickListener(this);
        init();
    }
    public void init() {
        try {
            if (getIntent().getExtras().getSerializable("newDetailModle") != null) {
                newDetailModle = (NewDetailModle) getIntent().getExtras().getSerializable(
                        "newDetailModle");
                imgList = newDetailModle.getImgList();
                titleString = newDetailModle.getTitle();

            } else {
                newModle = (NewModle) getIntent().getExtras().getSerializable("newModle");
                imgList = newModle.getImagesModle().getImgList();
                titleString = newModle.getTitle();
            }
            new_title.setText(titleString);
            adapter=new ImageDetailAdapter(imgList,ImageDetailActivity.this);
            image_viewpager.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_left:
                finish();
                break;
        }
    }
}
