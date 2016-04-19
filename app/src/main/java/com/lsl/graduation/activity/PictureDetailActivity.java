package com.lsl.graduation.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.lsl.graduation.R;
import com.lsl.graduation.Url;
import com.lsl.graduation.adapter.PictureDetailAdapter;
import com.lsl.graduation.bean.PictureDetailModel;
import com.lsl.graduation.json.PicuterSinaJson;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;

import java.util.List;

/**
 * Created by Forrest on 16/4/19.
 */
public class PictureDetailActivity extends BaseActivity implements View.OnClickListener{
    private ViewPager viewpager;
    private PictureDetailAdapter mAdapter;
    private  String imgUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_deatil);
        findViewById(R.id.linear_left).setOnClickListener(this);
        viewpager= (ViewPager) findViewById(R.id.viewpager_picture);


        if (getIntent().getExtras().getString("pic_id") != null) {
            imgUrl = getIntent().getExtras().getString("pic_id");

            loadData(Url.JINGXUANDETAIL_ID + imgUrl);
        }
    }

    private void loadData(String s) {
       showDialog();
        new StringContext().flag(LoadContext.FLAG_HTTP_FIRST).get(s)
                .listener(new SimpleLoadListener<String>() {
                    @Override
                    public void loadComplete(LoadContext<String> context) {
                        super.loadComplete(context);
                        getResult(context.getResult());
                        dismissDialog();
                    }

                    @Override
                    public void loadFail(LoadContext<String> context) {
                        super.loadFail(context);
                        dismissDialog();

                    }
                }).load();
    }
    public void getResult(String result) {
        try {
            List<PictureDetailModel> list = PicuterSinaJson.instance(this).readJsonPicuterModle(
                    result);
            mAdapter=new PictureDetailAdapter(PictureDetailActivity.this,list);
            viewpager.setAdapter(mAdapter);

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
