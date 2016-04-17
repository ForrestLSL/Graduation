package com.lsl.graduation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.R;
import com.lsl.graduation.bean.NewDetailModle;
import com.lsl.graduation.bean.NewModle;
import com.lsl.graduation.json.NewDetailJson;
import com.lsl.graduation.net.context.BitmapContext;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;
import com.lsl.graduation.utils.StringUtils;
import com.lsl.graduation.utils.UIHelper;
import com.lsl.graduation.widget.ProgressPieView;
import com.lsl.graduation.widget.htmltext.HtmlTextView;

/**
 * Created by Forrest on 16/4/16.
 */
public class NewsDetailActivity extends  BaseActivity implements View.OnClickListener{
    /** 新闻标题*/
    protected TextView new_title;
    /** 时间*/
    protected TextView new_time;
    /** 新闻详细内容*/
    protected HtmlTextView wb_details;
    /** 进度条*/
//    protected ProgressPieView progressPieView;
    /** 图片集合*/
    protected ImageView new_img;
    /** 图片数量*/
    protected TextView img_count;
    /** 播放按钮*/
    protected ImageView play;
    private String newUrl;
    private NewModle newModle;
    private String newID;
    private String imgCountString;
    private NewDetailModle newDetailModle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_news_detail);
        //得到前面传来的实体类
        newModle = (NewModle) getIntent().getExtras().getSerializable("newModle");
        newID = newModle.getDocid();
        newUrl = getUrl(newID);
        initView();
        initData(newUrl);

    }

    private void initView() {
        new_title= (TextView) findViewById(R.id.new_title);
        new_time= (TextView) findViewById(R.id.new_time);
        img_count= (TextView) findViewById(R.id.img_count);
        wb_details= (HtmlTextView) findViewById(R.id.wb_details);
//        progressPieView= (ProgressPieView) findViewById(R.id.progressPieView);
        new_img= (ImageView) findViewById(R.id.new_img);
        play= (ImageView) findViewById(R.id.play);
        findViewById(R.id.linear_left).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.tv_title);
        title.setTextColor(getResources().getColor(R.color.color_3));
    }
    private void initData(String url) {
        showDialog();
        new StringContext().flag(LoadContext.FLAG_HTTP_FIRST).get(url)
                .listener(new SimpleLoadListener<String>() {
                    @Override
                    public void loadComplete(LoadContext<String> context) {
                        super.loadComplete(context);
                        dismissDialog();
                        getResult(context.getResult());

                    }

                    @Override
                    public void loadFail(LoadContext<String> context) {
                        super.loadFail(context);
                        dismissDialog();
                        UIHelper.showMsg(NewsDetailActivity.this,"加载失败");
                    }
                }).load();

    }

    public void getResult(String result) {
        newDetailModle = NewDetailJson.instance(this).readJsonNewModles(result,
                newID);
        if (newDetailModle == null)
            return;
        if (!"".equals(newDetailModle.getUrl_mp4())) {
            new BitmapContext()
                    .get(newDetailModle.getCover())
                    .ImageView( new_img)
                    .flag(BitmapContext.FLAG_CACHE_FIRST)
                    .defaultImage(getResources().getDrawable(R.mipmap.default_image)).load();
            new_img.setVisibility(View.VISIBLE);
            new_img.setOnClickListener(this);
        } else {
            if (newDetailModle.getImgList().size() > 0) {
                imgCountString = "共" + newDetailModle.getImgList().size() + "张";
                new BitmapContext()
                        .get(newDetailModle.getImgList().get(0))
                        .ImageView( new_img)
                        .flag(BitmapContext.FLAG_CACHE_FIRST)
                        .defaultImage(getResources().getDrawable(R.mipmap.default_image)).load();
                new_img.setVisibility(View.VISIBLE);
                img_count.setVisibility(View.VISIBLE);
                img_count.setText(imgCountString);
                new_img.setOnClickListener(this);
            }
        }
        new_title.setText(newDetailModle.getTitle());
        new_time.setText("来源：" + newDetailModle.getSource() + " " + newDetailModle.getPtime());
        String content = newDetailModle.getBody();
        content = content.replace("<!--VIDEO#1--></p><p>", "");
        content = content.replace("<!--VIDEO#2--></p><p>", "");
        content = content.replace("<!--VIDEO#3--></p><p>", "");
        content = content.replace("<!--VIDEO#4--></p><p>", "");
        content = content.replace("<!--REWARD#0--></p><p>", "");
        wb_details.setHtmlFromString(content, false);



        if (!StringUtils.isEmpty(newDetailModle.getUrl_mp4())) {
            play.setVisibility(View.VISIBLE);
            img_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_img:
//                UIHelper.showMsg(NewsDetailActivity.this, "点击了视频播放");
                Intent intent=new Intent();
                intent.putExtra("newDetailModle", newDetailModle);
                Class clazz=null;
                if (!StringUtils.isEmpty(newDetailModle.getUrl_mp4())){
                    intent.putExtra("playUrl", newDetailModle.getUrl_mp4());
                    clazz=VideoPlayActivity.class;

                }else {
                    clazz=ImageDetailActivity.class;
                }
                intent.setClass(NewsDetailActivity.this,clazz);
                startActivity(intent);

                break;
            case R.id.linear_left:
                finish();
                break;
        }
    }
}
