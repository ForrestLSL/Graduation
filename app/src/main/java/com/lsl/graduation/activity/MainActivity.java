package com.lsl.graduation.activity;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lsl.graduation.Configs;
import com.lsl.graduation.R;
import com.lsl.graduation.fragment.NewsFragment;
import com.lsl.graduation.fragment.VideoFragment;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;
import com.lsl.graduation.net.http.HttpManager;
import com.lsl.graduation.utils.ReflectUtil;
import com.lsl.graduation.utils.UIHelper;
import com.lsl.graduation.widget.Title;
import com.lsl.graduation.widget.slide.NavigationDrawerItem;
import com.lsl.graduation.widget.slide.NavigationDrawerView;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    /**  侧滑菜单控件*/
    private DrawerLayout drawerLayout;
    /** 侧滑菜单中的ListView*/
    private NavigationDrawerView navigationDrawerList;
    /** 侧滑菜单中的Item*/
    private List<NavigationDrawerItem> navigationItems;
    /** */
    ListView leftDrawerListView;
    /** 是否显示侧滑菜单*/
    private boolean IsShow=true;
    /** 包含整个侧滑菜单的布局*/
    private LinearLayout linearDrawer;
    /** 标题*/
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        findViewById(R.id.linear_left).setOnClickListener(this);
        title= ((Title) findViewById(R.id.title)).title;//((HqTitle) findViewById(R.id.title)).title;
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        linearDrawer= (LinearLayout) findViewById(R.id.linearDrawer);
        navigationDrawerList= (NavigationDrawerView) findViewById(R.id.navigationDrawerList);
        leftDrawerListView= (ListView) findViewById(R.id.leftDrawerListView);
        // load data
        navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationDrawerItem("News", true));
        navigationItems.add(new NavigationDrawerItem("Video", true));
        navigationItems.add(new NavigationDrawerItem("Three",true));
        navigationItems.add(new NavigationDrawerItem("Four", true));
        navigationDrawerList.replaceWith(navigationItems);
        //add the List ItemClickListener
        leftDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (drawerLayout.isDrawerOpen(linearDrawer)) {
                    drawerLayout.closeDrawer(linearDrawer);
                    if (position==0){
                        changeFragment(NewsFragment.class.getName());
                    }else if (position==1){
                        changeFragment(VideoFragment.class.getName());
                    }
                    selectItem(position);
                }
            }
        });
        // add the drawerListener ,监听drawer的显示隐藏
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                IsShow = !IsShow;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                IsShow = !IsShow;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }
    private int currentSelectedPosition=0;
    private void selectItem(int position) {
        //设置选中和设置文字
        if (leftDrawerListView != null) {
            leftDrawerListView.setItemChecked(position, true);

            navigationItems.get(currentSelectedPosition).setSelected(false);
            navigationItems.get(position).setSelected(true);

            currentSelectedPosition = position;
            title.setText(navigationItems.get(position).getItemName());
        }

        if (linearDrawer != null) {
            drawerLayout.closeDrawer(linearDrawer);
        }
    }

    private void getHoSaVenue(){
        showDialog();
        new StringContext().flag(LoadContext.FLAG_HTTP_FIRST).post("http://123.56.162.207:8093/hosapro/hsvenue/findPageVenue")
                .param("rows", "10")
                .param("page","1")
                .param("xzb","116.403875")
                .param("yzb","39.915168")
                .listener(new SimpleLoadListener<String>() {
                    @Override
                    public void loadComplete(LoadContext<String> context) {
                        super.loadComplete(context);
                        dismissDialog();
                        JSONObject response;
                        try {
                            response = new JSONObject(context.getResult());
                            String total=response.getString("total");
                            UIHelper.showMsg(MainActivity.this,total);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).load();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_left:
                if (IsShow){
                    drawerLayout.openDrawer(linearDrawer);
                }else {
                    drawerLayout.closeDrawers();
                }

                break;
        }
    }
    private static String nowFragmentName;

    /**
     *  工具类fragment之间的切换
     * @param name
     */
    private void changeFragment(String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment nowFragment = fragmentManager
                .findFragmentByTag(nowFragmentName);
        if (nowFragment != null) {
            fragmentTransaction.hide(nowFragment);
        }
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (fragment != null && fragment.isAdded()) {
            fragmentTransaction.show(fragment);
            fragment.onResume();
        } else {
            try {
                fragment = (Fragment) ReflectUtil.getClass(name).newInstance();
                fragmentTransaction.add(R.id.contentFrame, fragment, name);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        nowFragmentName = name;
    }

}