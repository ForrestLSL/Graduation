package com.lsl.graduation.http.net;

/**
 * 关于Http请求回调接口
 * Created by Forrest on 16/3/30.
 */
public interface HttpGetListener {
    /** 准备加载*/
    void preLoad();
    /** 执行回调*/
    void postExecute();
    /** 失败回调*/
    void loadHttpFail();
    /** 成功回调*/
    void loadHttpSuccess();
}
