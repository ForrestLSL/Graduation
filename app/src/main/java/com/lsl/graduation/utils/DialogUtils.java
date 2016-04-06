package com.lsl.graduation.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lsl.graduation.R;

/**
 * Manager Create Dialog
 * Created by Forrest on 16/4/6.
 */
public class DialogUtils {
    public static Dialog createDialog(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.progress_bar, null);
        RelativeLayout layout= (RelativeLayout) view.findViewById(R.id.layout);
        Dialog dialog=new Dialog(context,R.style.loading_dialog_tran);
        dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return dialog;

    }
}
