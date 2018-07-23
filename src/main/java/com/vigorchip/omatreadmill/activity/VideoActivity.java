package com.vigorchip.omatreadmill.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.vigorchip.omatreadmill.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by wr-app1 on 2018/7/13.
 */

public class VideoActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private Button pause, start;
    private static long currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Log.e("This is VideoViewActivity onCreate is invoke!!!", String.valueOf(currentPosition));
        videoView = findViewById(R.id.videoView);
        pause = findViewById(R.id.pause);
        start = findViewById(R.id.start);
        pause.setOnClickListener(this);
        start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pause:
                mediaPlayer.pause();
                break;
            case R.id.start:
                mediaPlayer.start();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("This is VideoViewActivity onResume is invoke!!!", String.valueOf(currentPosition));
        videoView.setVideoURI(Uri.parse("/system/treadmill/video_01.mp4"));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                mp.setVolume(0.0f, 0.0f);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("This is VideoViewActivity onPause is invoke!!!", String.valueOf(videoView.getCurrentPosition()));
        if (mediaPlayer != null) {
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("This is VideoViewActivity onSaveInstanceState is invoke!!!", String.valueOf(0));

        currentPosition = mediaPlayer.getCurrentPosition();
        mediaPlayer.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("This is VideoViewActivity onRestoreInstanceState is invoke!!!", String.valueOf(0));
        mediaPlayer.seekTo(currentPosition);
    }
}
