package com.lsl.graduation.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.Configs;
import com.lsl.graduation.R;
import com.lsl.graduation.activity.ChannelActivity;
import com.lsl.graduation.adapter.NewsFragmentAdapter;
import com.lsl.graduation.bean.ChannelItem;
import com.lsl.graduation.db.ChannelManager;
import com.lsl.graduation.utils.DeviceUtils;
import com.lsl.graduation.utils.UIHelper;

import java.sql.SQLException;
import java.util.ArrayList;

import static com.lsl.graduation.R.drawable.channel_item_bg;

/**
 * Created by Forrest on 16/4/9.
 */
public class NewsFragment extends  BaseFragment implements View.OnClickListener{
    /** 频道Item容器*/
    private LinearLayout linear;
    /** 容纳Fragment的布局*/
    private FrameLayout frame;
    /** 容纳Item的横滑容器*/
    private HorizontalScrollView horizontal;
    /** 频道添加*/
    private ImageView add;
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    /** 频道列表*/
    private static ArrayList<ChannelItem> userChannelLists;
    /** 当前选中Item*/
    private int columnSelectIndex;
    /** 当前选中Item*/
    private ViewPager viewpager;
    /**  Fragment 集合*/
    private ArrayList<Fragment> fragments;
    private NewsFragmentAdapter mAdapetr;
    private Fragment newfragment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, null);
        initView(view);

        return view;
    }



    /**
     * 初始化组件
     * @param view
     */
    private void initView(View view) {
        linear= (LinearLayout) view.findViewById(R.id.content);
        horizontal= (HorizontalScrollView) view.findViewById(R.id.horizontal);
        add= (ImageView) view.findViewById(R.id.add);
        add.setOnClickListener(this);
        viewpager= (ViewPager) view.findViewById(R.id.viewpager);

        mScreenWidth= DeviceUtils.getDeviceWidth(getActivity());
        mItemWidth=mScreenWidth/7;//一个Item所占的宽度是屏幕的七分之一
        fragments=new ArrayList<Fragment>();

        initViewPager();
        initColumnData();


    }
    /**
     * 初始化FragmentViewPager
     */
    private void initViewPager() {
        mAdapetr=new NewsFragmentAdapter(getFragmentManager());
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mAdapetr);
        viewpager.setOnPageChangeListener(pagerListener);
    }
    /** 获取Column栏目 数据 */
    private void initColumnData() {
        try {
            userChannelLists = ((ArrayList<ChannelItem>) ChannelManager.getManage(
                    AppContext.getInstance().getSqlHelper()).getUserChannel());
            initFragment();
            intitTabView();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void intitTabView(){
        linear.removeAllViews();
        for (int i = 0; i <userChannelLists.size() ; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(getActivity());
            columnTextView.setTextAppearance(getActivity(), R.style.tv_item);
            columnTextView.setBackgroundResource(channel_item_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelLists.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(
                    R.color.white));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            final int finalI = i;
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    UIHelper.showMsg(getActivity(),userChannelLists.get(finalI).getName());
                    for (int i = 0; i < linear.getChildCount(); i++) {
                        View localView = linear.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            viewpager.setCurrentItem(i);
                        }
                    }
                }
            });
            linear.addView(columnTextView, i, params);
        }
    }

    private void initFragment() {
        fragments.clear();

        for (int i = 0; i <userChannelLists.size(); i++) {
            String nameString = userChannelLists.get(i).getName();
            fragments.add(initItemFragment(nameString));
        }
        mAdapetr.appendList(fragments);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                Intent intent=new Intent(getActivity(), ChannelActivity.class);
                getActivity().startActivityForResult(intent, Configs.REQUEST_CODE);
                break;
        }
    }



    public ViewPager.OnPageChangeListener pagerListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            viewpager.setCurrentItem(position);
            selectTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * 选择 horizon里的Item
     * @param position
     */
    private void selectTab(int position){
        columnSelectIndex=position;
        for (int i=0;i<linear.getChildCount();i++){
            View checkView=linear.getChildAt(position);//得到选中的View
            int k=checkView.getMeasuredWidth();//得到选中View的宽度
            int l=checkView.getLeft();//得到选中VIew的左边距离
            int i2=l+k/2-mScreenWidth/2;//将View移到中间位置
            horizontal.smoothScrollTo(i2,0);//滑动到相关位置
        }
        //判断是否选中
        for (int i = 0; i <linear.getChildCount() ; i++) {
            View  checkView=linear.getChildAt(i);
            boolean ischeck;
            ischeck = i == position;
            checkView.setSelected(ischeck);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Configs.REQUEST_CODE){
            viewpager.removeAllViews();
            initColumnData();
            UIHelper.showMsg(getActivity(),"得到了消息");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    public Fragment initItemFragment(String channelName) {
        if (channelName.equals("头条")) {
            newfragment = new HeadFragment();
        } else if (channelName.equals("足球")) {
            newfragment = new FoodBallFragment();
        } else if (channelName.equals("娱乐")) {
            newfragment = new YuLeFragment();
        } else if (channelName.equals("体育")) {
            newfragment = new TiYuFragment();
        } else if (channelName.equals("财经")) {
            newfragment = new CaiJingFragment();
        } else if (channelName.equals("科技")) {
            newfragment = new KeJiFragment();
        } else if (channelName.equals("电影")) {
            newfragment = new DianYingFragment();
        } else if (channelName.equals("汽车")) {
            newfragment = new QiCheFragment();
        } else if (channelName.equals("笑话")) {
            newfragment = new XiaoHuaFragment();
        } else if (channelName.equals("时尚")) {
            newfragment = new ShiShangFragment();
        } else if (channelName.equals("北京")) {
            newfragment = new BeiJingFragment();
        } else if (channelName.equals("军事")) {
            newfragment = new JunShiFragment();
        } else if (channelName.equals("房产")) {
            newfragment = new FangChanFragment();
        } else if (channelName.equals("游戏")) {
            newfragment = new YouXiFragment();
        } else if (channelName.equals("情感")) {
            newfragment = new QinGanFragment();
        } else if (channelName.equals("精选")) {
            newfragment = new JingXuanFragment();
        } else if (channelName.equals("电台")) {
            newfragment = new DianTaiFragment();
        }
//        else if (channelName.equals("图片")) {
//            newfragment = new TuPianFragment();
//        }
        else if (channelName.equals("NBA")) {
            newfragment = new NBAFragment();
        } else if (channelName.equals("数码")) {
            newfragment = new ShuMaFragment();
        } else if (channelName.equals("移动")) {
            newfragment = new YiDongFragment();
        } else if (channelName.equals("彩票")) {
            newfragment = new CaiPiaoFragment();
        } else if (channelName.equals("教育")) {
            newfragment = new JiaoYuFragment();
        } else if (channelName.equals("论坛")) {
            newfragment = new LunTanFragment();
        } else if (channelName.equals("旅游")) {
            newfragment = new LvYouFragment();
        } else if (channelName.equals("手机")) {
            newfragment = new ShouJiFragment();
        } else if (channelName.equals("博客")) {
            newfragment = new BoKeFragment();
        } else if (channelName.equals("社会")) {
            newfragment = new SheHuiFragment();
        } else if (channelName.equals("家居")) {
            newfragment = new JiaJuFragment();
        } else if (channelName.equals("暴雪")) {
            newfragment = new BaoXueYouXiFragment();
        } else if (channelName.equals("亲子")) {
            newfragment = new QinZiFragment();
        } else if (channelName.equals("CBA")) {
            newfragment = new CBAFragment();
        }
        return newfragment;
    }
}
