package com.lsl.graduation.widget.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.lsl.graduation.R;

import java.util.List;


/**
 * Created by Michal Bialas on 19/07/14.
 */
public class NavigationDrawerView extends BetterViewAnimator {

    ListView leftDrawerListView;

    private final NavigationDrawerAdapter adapter;


    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new NavigationDrawerAdapter(context);
    }

    public void replaceWith(List<NavigationDrawerItem> items) {
        adapter.replaceWith(items);
        setDisplayedChildId(R.id.leftDrawerListView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        leftDrawerListView= (ListView) findViewById(R.id.leftDrawerListView);
        leftDrawerListView.setAdapter(adapter);
    }

    public NavigationDrawerAdapter getAdapter() {
        return adapter;
    }
}
