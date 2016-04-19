package com.lsl.graduation.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lsl.graduation.AppContext;
import com.lsl.graduation.R;
import com.lsl.graduation.utils.MLog;
import com.lsl.graduation.utils.UIHelper;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Forrest on 16/4/19.
 */
public class OtherFragment extends BaseFragment implements View.OnClickListener{
    private LinearLayout clear;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_other,null);
        clear= (LinearLayout) view.findViewById(R.id.clear);
        clear.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clear:
                File files=new File(Environment.getExternalStorageDirectory()
                        +"/Android/data/"+ AppContext.getInstance().getPackageName()  );//"+/cache/"
                File cacheFile = new File(AppContext.getInstance().getCacheDir()+"graduation");


                MLog.d("Graduation", files.getAbsolutePath().toString()+"aaaa"+files.length());
                MLog.d("Graduation", cacheFile.getAbsolutePath().toString()+"aaaa"+cacheFile.length());

                try {
                    deleteAllFiles(files);
                    deleteAllFiles(cacheFile);
                    UIHelper.showMsg(getActivity(),"缓存已清除");
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
        }
    }
    /**
     * 删除某个目录下所有的文件
     *
     * @param root
     */
    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}
