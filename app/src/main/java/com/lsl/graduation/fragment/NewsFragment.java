package com.lsl.graduation.fragment;

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
import com.lsl.graduation.R;
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

        initFragment();

    }
    /**
     * 初始化FragmentViewPager
     */
    private void initViewPager() {
        mAdapetr=new NewsFragmentAdapter(getFragmentManager());
        viewpager.setAdapter(mAdapetr);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setOnPageChangeListener(pagerListener);
    }
    /** 获取Column栏目 数据 */
    private void initColumnData() {
        try {
            userChannelLists = ((ArrayList<ChannelItem>) ChannelManager.getManage(
                    AppContext.getInstance().getSqlHelper()).getUserChannel());
            intitTabView();
            initFragment();
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
            fragments.add(new VideoFragment());
        }
        mAdapetr.appendList(fragments);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                UIHelper.showMsg(getActivity(),"点击了添加");
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

}
