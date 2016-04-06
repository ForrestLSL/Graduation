package com.lsl.graduation.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsl.graduation.Configs;
import com.lsl.graduation.R;
import com.lsl.graduation.net.context.LoadContext;
import com.lsl.graduation.net.context.StringContext;
import com.lsl.graduation.net.loadlistener.SimpleLoadListener;
import com.lsl.graduation.net.http.HttpManager;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.xml.transform.Result;

public class MainActivity extends BaseActivity {
    private Button button;
    private TextView textView;
    private HttpResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        textView= (TextView) findViewById(R.id.text);
        Configs.Init(this);
        getHoSaVenue();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Object,Object,Result>(){

                    @Override
                    protected Result doInBackground(Object... params) {
                        try {
                            response =HttpManager.executeHttpPost("https://www.baidu.com", null, null);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Result result) {
                        super.onPostExecute(result);
                        textView.setText("返回结果"+response.getEntity().toString());
                    }
                }.execute();
            }
        });

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
                        JSONObject resqponse;
                        try {
                            resqponse = new JSONObject(context.getResult());
                            String total=resqponse.getString("total");
                            textView.setText("返回结果"+total);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).load();
    }
}
