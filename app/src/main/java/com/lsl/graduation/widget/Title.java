package com.lsl.graduation.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.R;

/**
 * Created by Forrest on 16/4/7.
 */
public class Title extends RelativeLayout{
    private View content;
    public TextView left;
    public TextView right;
    public TextView title;
    private LinearLayout back;
    public Title(Context context) {
        super(context);
    }

    public Title(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);

    }

    private void initView(AttributeSet attrs) {
        TypedArray array=getContext().obtainStyledAttributes(attrs, R.styleable.Title);
        content = LayoutInflater.from(getContext()).inflate(R.layout.main_title, null);//obtain the layout
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, AppContext.deviceWidth / 7);// set the layoutParams
        content.setBackgroundColor(array.getColor(R.styleable.Title_back_color,Color.TRANSPARENT));//set the background color Color.TRANSPARENT
        addView(content, params);//add the title into the RelativeLayout
        //set the attribute
        left= (TextView) findViewById(R.id.tv_left);
        left.setBackgroundColor(Color.TRANSPARENT);
        left.setText(array.getString(R.styleable.Title_text_left));
        left.setPadding(array.getDimensionPixelSize(R.styleable.Title_text_margin_left, 0), 0, 0, 0);
        left.setBackgroundResource(array.getResourceId(R.styleable.Title_res_left_btn, 0));
        right= (TextView) findViewById(R.id.tv_right);

        right.setBackgroundColor(Color.TRANSPARENT);
        right.setText(array.getString(R.styleable.Title_text_right));
        right.setPadding(0, 0, array.getDimensionPixelSize(R.styleable.Title_text_margin_right, 0), 0);
        right.setBackgroundResource(array.getResourceId(R.styleable.Title_res_right_btn, 0));

        title= (TextView) findViewById(R.id.tv_title);
        title.setTextColor(getResources().getColor(array.getResourceId(R.styleable.Title_text_color, R.color.white)));
        title.setText(array.getString(R.styleable.Title_text));

    }

    public Title(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);

    }
    public void setTitle(String title) {
        this.title.setText(title);
    }

    public TextView getTitleView() {
        return title;
    }

    public void setTitleColor(int id) {
        this.title.setTextColor(getResources().getColor(id));
    }

}
