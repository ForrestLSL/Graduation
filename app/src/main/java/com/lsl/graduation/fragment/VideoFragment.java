package com.lsl.graduation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lsl.graduation.R;
import com.lsl.graduation.adapter.NewsFragmentAdapter;

import java.util.ArrayList;

/**
 * Created by Forrest on 16/4/9.
 */
public class VideoFragment extends BaseFragment {
    private ViewPager viewpager;
    private RadioGroup radio_group;
    private NewsFragmentAdapter mAdapter;
    private ArrayList<Fragment> fragmentList;
    protected RadioButton mJingXuan;
    protected RadioButton mQuTu;
    protected RadioButton mGuShi;
    protected RadioButton mMeiTu;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, null);
        viewpager= (ViewPager) view.findViewById(R.id.viewpager_video);
        radio_group= (RadioGroup) view.findViewById(R.id.radio_group);
        mJingXuan= (RadioButton) view.findViewById(R.id.jingxuan);
        mJingXuan.setText("热点");
        mQuTu= (RadioButton) view.findViewById(R.id.qutu);
        mQuTu.setText("娱乐");
        mGuShi= (RadioButton) view.findViewById(R.id.gushi);
        mGuShi.setText("搞笑");
        mMeiTu= (RadioButton) view.findViewById(R.id.meitu);
        mMeiTu.setText("精品");
        initViewPager();
        return view;

    }

    private void initViewPager() {
        fragmentList=new ArrayList<Fragment>();
        fragmentList.add(new VideoReDianFragment());
        fragmentList.add(new VideoYuLeFragment());
        fragmentList.add(new VideoGaoXiaoFragment());
        fragmentList.add(new VideoJingPinFragment());
        mAdapter=new NewsFragmentAdapter(getFragmentManager(),fragmentList);
        viewpager.setOffscreenPageLimit(1);
        viewpager.setAdapter(mAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.jingxuan:
                        viewpager.setCurrentItem(0);
                        setRadioButtonCheck(true, false, false, false);
                        break;
                    case R.id.meitu:
                        viewpager.setCurrentItem(1);
                        setRadioButtonCheck(false, true, false, false);
                        break;
                    case R.id.gushi:
                        viewpager.setCurrentItem(2);
                        setRadioButtonCheck(false, false, true, false);
                        break;
                    case R.id.qutu:
                        viewpager.setCurrentItem(3);
                        setRadioButtonCheck(false, false, false, true);
                        break;
                }
            }
        });
    }
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            viewpager.setCurrentItem(position);
            switch (position) {
                case 0:
                    setRadioButtonCheck(true, false, false, false);
                    break;
                case 1:
                    setRadioButtonCheck(false, true, false, false);
                    break;
                case 2:
                    setRadioButtonCheck(false, false, true, false);
                    break;
                case 3:
                    setRadioButtonCheck(false, false, false, true);
                    break;
            }
        }

    }

    private void setRadioButtonCheck(boolean b, boolean c, boolean d, boolean e) {
        mJingXuan.setChecked(b);
        mQuTu.setChecked(e);
        mMeiTu.setChecked(c);
        mGuShi.setChecked(d);
    }
}
