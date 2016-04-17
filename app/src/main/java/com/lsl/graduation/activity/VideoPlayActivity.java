package com.lsl.graduation.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lsl.graduation.R;
import com.lsl.graduation.utils.UIHelper;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by Forrest on 16/4/17.
 */
public class VideoPlayActivity extends BaseActivity implements MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    protected VideoView mVideoView;
    protected ProgressBar mProgressBar;
    protected TextView mLoadRate;
    protected ImageView mVideoEnd;
    private Uri uri;
    private String playUrl;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // hide titlebar of application
        // must be before setting the layout
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // hide statusbar of Android
        // could also be done later
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        mVideoView= (VideoView) findViewById(R.id.buffer);
        mProgressBar= (ProgressBar) findViewById(R.id.probar);
        mLoadRate= (TextView) findViewById(R.id.load_rate);
        mVideoEnd= (ImageView) findViewById(R.id.video_end);

        try {
            if (!LibsChecker.checkVitamioLibs(this))
                return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        playUrl = getIntent().getExtras().getString("playUrl");
        title = getIntent().getExtras().getString("filename");
        if ("".equals(playUrl) || playUrl == null) {
            UIHelper.showMsg(VideoPlayActivity.this,"请求地址错误");
            finish();
        }
        uri = Uri.parse(playUrl);
        mVideoView.setVideoURI(uri);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(this);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mLoadRate.setText(percent + "%");
        mVideoView.setFileName(title);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        System.out.println(what);
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    mProgressBar.setVisibility(View.VISIBLE);
                    mLoadRate.setText("");
                    mLoadRate.setVisibility(View.VISIBLE);
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                // mVideoEnd.setVisibility(View.VISIBLE);
                mVideoView.start();
                mProgressBar.setVisibility(View.GONE);
                mLoadRate.setVisibility(View.GONE);
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setPlaybackSpeed(1.0f);
    }
}
