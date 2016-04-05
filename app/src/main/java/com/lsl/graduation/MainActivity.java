package com.lsl.graduation;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsl.graduation.net.HttpManager;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.logging.Handler;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private HttpResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button);
        textView= (TextView) findViewById(R.id.text);
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
}
