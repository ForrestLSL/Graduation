package com.lsl.graduation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lsl.graduation.activity.BaseActivity;

/**
 * Created by Forrest on 16/4/9.
 */
public class BaseFragment extends Fragment{
    protected BaseActivity self;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        self= (BaseActivity) getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
