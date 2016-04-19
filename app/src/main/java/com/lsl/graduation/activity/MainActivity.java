package com.lsl.graduation.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.fragment.NewsFragment;
import com.lsl.graduation.fragment.PictureFragment;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;
import com.lsl.graduation.utils.ReflectUtil;
import com.lsl.graduation.utils.UIHelper;
import com.lsl.graduation.widget.Title;
import com.lsl.graduation.widget.slide.NavigationDrawerItem;
import com.lsl.graduation.widget.slide.NavigationDrawerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    private View menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        changeFragment(NewsFragment.class.getName());
        selectItem(0);

    }

    private void initView() {
        menu= findViewById(R.id.linear_left);
        menu.setOnClickListener(this);
        title= ((Title) findViewById(R.id.title)).title;//((HqTitle) findViewById(R.id.title)).title;
        drawerLayout= (DrawerLayout) findViewById(R.id.drawerLayout);
        linearDrawer= (LinearLayout) findViewById(R.id.linearDrawer);
        navigationDrawerList= (NavigationDrawerView) findViewById(R.id.navigationDrawerList);
        leftDrawerListView= (ListView) findViewById(R.id.leftDrawerListView);
        // load data
        navigationItems = new ArrayList<>();
        navigationItems.add(new NavigationDrawerItem("新闻", true));
        navigationItems.add(new NavigationDrawerItem("图片", true));
        navigationItems.add(new NavigationDrawerItem("视频",true));
        navigationItems.add(new NavigationDrawerItem("其他", true));
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
                        changeFragment(PictureFragment.class.getName());
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
                RotateAnimation animation=new RotateAnimation(0,180,menu.getWidth()/2,menu.getHeight()/2);
                animation.setDuration(1000);
                menu.startAnimation(animation);
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
        FragmentManager  fragmentManager = getSupportFragmentManager();
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
    private long exitTime=0;

    /***
     *  退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-exitTime>2000){
                UIHelper.showMsg(this,"再按一次退出程序");
                exitTime=System.currentTimeMillis();
            }else System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager  fragmentManager = getSupportFragmentManager();
        Fragment f = fragmentManager.findFragmentByTag(nowFragmentName);
        f.onActivityResult(requestCode, resultCode, data);

    }
}
