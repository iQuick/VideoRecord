package me.imli.lib_movierecorder.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.imli.lib_movierecorder.R;
import me.imli.lib_movierecorder.widget.VideoPreviewView;

/**
 * Created by Doots on 2016/10/11.
 */
public class VideoPreviewActivity extends AppCompatActivity {

    private static final String INTENT_VIDEO_PATH = "INTENT_VIDEO_PATH";

    private VideoPreviewView mVideoPreview;

    public static final void startVideoPreview(final Context context, final String path) {
        Intent intent = new Intent(context, VideoPreviewActivity.class);
        intent.putExtra(INTENT_VIDEO_PATH, path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        findView();
        playVideo();
    }

    /**
     *
     */
    private void findView() {
        mVideoPreview = (VideoPreviewView) findViewById(R.id.video_preview);
        findViewById(R.id.root).setOnClickListener(rootClick());
    }

    /**
     *
     */
    private void playVideo() {
        String path = getIntent().getStringExtra(INTENT_VIDEO_PATH);
        mVideoPreview.setVideoPath(path);
        mVideoPreview.setOnVideoPlayListener(onVideoPlayListener());
        mVideoPreview.setOnVideoBufferListener(onVideoBufferListener());
        mVideoPreview.start();
    }

    /**
     * 点击根布局监听
     * @return
     */
    private View.OnClickListener rootClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };
    }

    /**
     *
     * @return
     */
    private VideoPreviewView.OnVideoPlayListener onVideoPlayListener() {
        return new VideoPreviewView.OnVideoPlayListener() {
            @Override
            public void onProgress(VideoPreviewView view, int play, int duration) {
                Log.d(getClass().getSimpleName(), "progress ==>" + play / duration + "play ==>> " + play + "   duration ==> " + duration);
            }
        };
    }

    /**
     *
     * @return
     */
    private VideoPreviewView.OnVideoBufferListener onVideoBufferListener() {
        return new VideoPreviewView.OnVideoBufferListener() {
            @Override
            public void onBuffering(VideoPreviewView view, int progress) {

            }
        };
    }

    @Override
    protected void onResume() {
        mVideoPreview.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mVideoPreview.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mVideoPreview.stopPlayback();
        super.onDestroy();
    }
}
