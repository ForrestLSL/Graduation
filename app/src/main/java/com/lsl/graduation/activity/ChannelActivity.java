package com.lsl.graduation.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.Configs;
import com.lsl.graduation.R;
import com.lsl.graduation.adapter.MyGridLayoutManager;
import com.lsl.graduation.adapter.UserAdapter;
import com.lsl.graduation.bean.ChannelItem;
import com.lsl.graduation.db.ChannelManager;
import com.lsl.graduation.utils.UIHelper;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Forrest on 16/4/18.
 */
public class ChannelActivity extends BaseActivity {
    private RecyclerView user_recycler;
    private RecyclerView other_recycler;
    private UserAdapter userAdapter;
    private UserAdapter otherAdapter;
    /**
     * 其它栏目列表
     */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    /**
     * 用户栏目列表
     */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        user_recycler= (RecyclerView) findViewById(R.id.user_recycler);
        other_recycler= (RecyclerView) findViewById(R.id.other_recycler);
        initData();
        initLisetener();
        findViewById(R.id.linear_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Configs.RESULT_CODE);
                finish();
            }
        });

    }



    private void initData() {
        try {
            //用户已选频道
            userChannelList = ((ArrayList<ChannelItem>) ChannelManager.getManage(AppContext.getInstance().getSqlHelper()).getUserChannel());
            userAdapter=new UserAdapter(this,userChannelList);
            MyGridLayoutManager manager=new MyGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
            user_recycler.setLayoutManager(manager);
            user_recycler.setAdapter(userAdapter);
            user_recycler.setItemAnimator(new DefaultItemAnimator());
            //未选择频道
            otherChannelList = ((ArrayList<ChannelItem>) ChannelManager.getManage(AppContext.getInstance().getSqlHelper()).getOtherChannel());
            otherAdapter=new UserAdapter(this,otherChannelList);
            MyGridLayoutManager manager1=new MyGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
            other_recycler.setLayoutManager(manager1);
            other_recycler.setAdapter(otherAdapter);
            other_recycler.setItemAnimator( new DefaultItemAnimator());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void initLisetener() {
        userAdapter.setOnItemClickLitener(new UserAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, final int position) {
                final ChannelItem selectItem;
                selectItem= userChannelList.get(position);
                otherChannelList.add(selectItem);
                userChannelList.remove(position);
                userAdapter.notifyDataSetChanged();
                otherAdapter.notifyDataSetChanged();
                try {
                    ChannelManager.getManage(AppContext.getInstance().getSqlHelper()).updateChannel(selectItem, "0");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


        otherAdapter.setOnItemClickLitener(new UserAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                final ChannelItem selectItem;
                selectItem= otherChannelList.get(position);
                userChannelList.add(selectItem);
                otherChannelList.remove(position);
                userAdapter.notifyDataSetChanged();
                otherAdapter.notifyDataSetChanged();
                try {
                    ChannelManager.getManage(AppContext.getInstance().getSqlHelper()).updateChannel(selectItem, "1");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            setResult(Configs.RESULT_CODE);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
