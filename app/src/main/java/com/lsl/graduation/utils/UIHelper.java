package com.lsl.graduation.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Forrest on 16/4/5.
 */
public class UIHelper {
    private static Toast toast;
    /**
     * 显示消息
     * @param context
     * @param msg
     */
    public static void showMsg(Context context,String msg){
        if(toast == null){
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else{
            toast.setText(msg);
        }
        toast.show();
    }
}
