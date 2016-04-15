package com.lsl.graduation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.Url;
import com.lsl.graduation.adapter.NewsAdapter;
import com.lsl.graduation.bean.NewModle;
import com.lsl.graduation.json.NewListJson;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;
import com.lsl.graduation.utils.MLog;
import com.lsl.graduation.utils.UIHelper;
import com.lsl.graduation.widget.HeaderView;
import com.lsl.graduation.widget.water.WaterDropListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Forrest on 16/4/14.
 */
public class HeadFragment extends BaseFragment implements WaterDropListView.IWaterDropListViewListener {
    private WaterDropListView water_list;
    private HeaderView headerView;
    private List<NewModle> datas;
    private int index = 0;
    private NewsAdapter mAdapter;
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
        initData();



        datas=new ArrayList<>();
//        headerView=new HeaderView(getActivity(),datas);
        return view;

    }

    private void initData() {
        getMyActivity().showDialog();
        new StringContext().flag(LoadContext.FLAG_HTTP_FIRST).get(getNewUrl(index + ""))
                .listener(new SimpleLoadListener<String>() {
                    @Override
                    public void loadComplete(LoadContext<String> context) {
                        super.loadComplete(context);
                        getMyActivity().dismissDialog();
                        getResult(context.getResult());
//                        JSONObject response;
//                        try {
//                            response = new JSONObject(context.getResult());
//                            String total=response.getString("total");
//                            UIHelper.showMsg(getMyActivity(), total);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }).load();

    }
    public void getResult(String result) {
        List<NewModle> list =
                NewListJson.instance(getActivity()).readJsonNewModles(result,
                        Url.TopId);
        mAdapter.clear();
        datas.addAll(list);
        mAdapter.appendList(datas);
//        UIHelper.showMsg(getMyActivity(),list.get(0).getTitle());

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
