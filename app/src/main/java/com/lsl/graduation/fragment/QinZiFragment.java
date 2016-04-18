package com.lsl.graduation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsl.graduation.R;
import com.lsl.graduation.Url;
import com.lsl.graduation.adapter.NewsAdapter;
import com.lsl.graduation.bean.NewModle;
import com.lsl.graduation.json.NewListJson;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;
import com.lsl.graduation.widget.HeaderView;
import com.lsl.graduation.widget.water.WaterDropListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/14.
 */
public class QinZiFragment extends BaseFragment implements WaterDropListView.IWaterDropListViewListener {
    /** 列表*/
    private WaterDropListView water_list;
    private HeaderView headerView;
    /** 数据*/
    private List<NewModle> datas;
    /** 页数*/
    private int index = 0;
    /** 适配器*/
    private NewsAdapter mAdapter;
    /** 刷新标志*/
    private boolean isRefresh = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment,null);
        water_list= (WaterDropListView) view.findViewById(R.id.water_list);
        datas=new ArrayList<NewModle>();
        mAdapter=new NewsAdapter(getMyActivity(),datas);
        water_list.setAdapter(mAdapter);
        water_list.setWaterDropListViewListener(this);
        water_list.setPullLoadEnable(true);
        initData(index);
        datas=new ArrayList<>();
//        headerView=new HeaderView(getActivity(),datas);
        return view;

    }

    private void initData( int page) {
        getMyActivity().showDialog();
        new StringContext().flag(LoadContext.FLAG_HTTP_FIRST).get(getCommonUrl(page + "", Url.QinZiId))
                .listener(new SimpleLoadListener<String>() {
                    @Override
                    public void loadComplete(LoadContext<String> context) {
                        super.loadComplete(context);
                        getMyActivity().dismissDialog();
                        getResult(context.getResult());

                    }

                    @Override
                    public void loadFail(LoadContext<String> context) {
                        super.loadFail(context);
                        getMyActivity().dismissDialog();

                    }
                }).load();

    }
    public void getResult(String result) {
        List<NewModle> list =
                NewListJson.instance(getActivity()).readJsonNewModles(result,
                        Url.QinZiId);
        if (isRefresh){
            isRefresh=false;
            mAdapter.clear();
            datas.clear();

        }
        mAdapter.appendList(list);
    }



    @Override
    public void onRefresh() {
        isRefresh = true;
        index=0;
        initData(index);
        water_list.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        index = index + 20;
        initData(index);
        water_list.stopLoadMore();
    }
}
