package com.lsl.graduation.widget.slide;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.graduation.R;


/**
 * Created by Michal Bialas on 19/07/14.
 */
public class NavigationDrawerItemView extends RelativeLayout {

    RelativeLayout rr;

    TextView itemTitleTV;

    ImageView itemIconIV;

    final Resources res;


    public NavigationDrawerItemView(Context context) {
        super(context);
        res = context.getResources();

    }

    public NavigationDrawerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        res = context.getResources();
    }

    public NavigationDrawerItemView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
        res = context.getResources();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rr= (RelativeLayout) findViewById(R.id.itemRR);
        itemTitleTV= (TextView) findViewById(R.id.navigationDrawerItemTitleTV);
        itemIconIV= (ImageView) findViewById(R.id.navigationDrawerItemIconIV);
    }

    public void bindTo(NavigationDrawerItem item) {
        requestLayout();
        if (item.isMainItem()) {
            itemTitleTV.setText(item.getItemName());
            itemTitleTV.setTextSize(22);
            itemIconIV.setVisibility(View.GONE);
        } else {
            itemTitleTV.setText(item.getItemName());
            itemTitleTV.setTextSize(14);
            itemIconIV.setImageDrawable(getIcon(item.getItemIcon()));
            itemIconIV.setVisibility(View.VISIBLE);
            rr.setBackgroundColor(res.getColor(R.color.color_e));
        }

        if(item.isSelected()) {
            itemTitleTV.setTypeface(null, Typeface.BOLD);
        } else {
            itemTitleTV.setTypeface(null, Typeface.NORMAL);
        }

    }

    private Drawable getIcon(int res) {
        return getContext().getResources().getDrawable(res);
    }
}
