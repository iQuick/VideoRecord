package me.imli.lib_movierecorder.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import me.imli.lib_magicprogressbar.MagicProgressBar;
import me.imli.lib_movierecorder.R;
import me.imli.lib_movierecorder.listener.OnRecorderErrorListener;
import me.imli.lib_movierecorder.listener.OnVideoRecorderListener;
import me.imli.lib_movierecorder.widget.RecordButton;
import me.imli.lib_movierecorder.widget.VideoRecorderView;

/**
 * Created by Doots on 2016/10/10.
 */
public class VideoRecorderActivity extends AppCompatActivity {

    // INTENT_PATH
    public static final String INTENT_PATH = "path";

    private VideoRecorderView mRecordView;
    private RecordButton mRecordButton;
    private TextView mTipLoosen;
    private TextView mTipGlide;
    private MagicProgressBar mPB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder_video);
        findView();
        initView();
        setListener();
    }

    /**
     * 查找控件
     */
    private void findView() {
        mRecordView = (VideoRecorderView) findViewById(R.id.vr_record);
        mRecordButton = (RecordButton) findViewById(R.id.btn_record);
        mTipLoosen = (TextView) findViewById(R.id.tv_loosen_record);
        mTipGlide = (TextView) findViewById(R.id.tv_up_glide_record);
        mPB = (MagicProgressBar) findViewById(R.id.pb_record);

    }

    /**
     * 初始化
     */
    private void initView() {
        mTipGlide.setVisibility(View.GONE);
        mTipLoosen.setVisibility(View.GONE);
        mPB.setVisibility(View.GONE);
        mPB.setProgress(getProgress(0));
    }


    /**
     * 设置监听器
     */
    private void setListener() {
        mRecordView.setOnRecorderErrorListener(new OnRecorderErrorListener());
        mRecordView.setOnVideoRecorderListener(onVideoRecorderListener());
        mRecordButton.setOnTouchRecordListener(onTouchRecordListener());
    }

    /**
     * 获取进度
     * @param progress
     * @return
     */
    private int getProgress(int progress) {
        return 100 - progress;
    }

    /**
     * 视频录制监听
     * @return
     */
    private OnVideoRecorderListener onVideoRecorderListener() {
        return new OnVideoRecorderListener() {
            @Override
            public void onRecorderProgress(VideoRecorderView view, int progress) {
                mPB.setProgress(getProgress(progress));
            }

            @Override
            public void onComplete(VideoRecorderView view, String path, long recordTime) {
                Intent intent = new Intent();
                intent.putExtra(INTENT_PATH, path);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onCancel(VideoRecorderView view) {

            }
        };
    }


    /**
     * 按钮按下监听
     * @return
     */
    private RecordButton.OnTouchRecordListener onTouchRecordListener() {
        return new RecordButton.OnTouchRecordListener() {
            @Override
            public void onStateChange(RecordButton btn, RecordButton.RecordState state) {
                if (RecordButton.RecordState.READY_CANCEL == state) {
                    mTipGlide.setVisibility(View.GONE);
                    mTipLoosen.setVisibility(View.VISIBLE);
                    mRecordView.setCancelTimeOut(true);
                    mPB.setFillColor(getColorById(R.color.pb_loosen));
                } else if (RecordButton.RecordState.RECODEING == state) {
                    mTipGlide.setVisibility(View.VISIBLE);
                    mTipLoosen.setVisibility(View.GONE);
                    mRecordView.setCancelTimeOut(false);
                    mPB.setFillColor(getColorById(R.color.pb_normal));
                }
            }

            @Override
            public void onDown(RecordButton btn) {
                mTipGlide.setVisibility(View.VISIBLE);
                mTipLoosen.setVisibility(View.GONE);
                mPB.setVisibility(View.VISIBLE);
                mPB.setProgress(getProgress(0));
                mPB.setFillColor(getColorById(R.color.pb_normal));
                // 开始录制
                mRecordView.startRecord();
            }

            @Override
            public void onUp(RecordButton btn, RecordButton.RecordState state) {
                mTipGlide.setVisibility(View.GONE);
                mTipLoosen.setVisibility(View.GONE);
                mPB.setVisibility(View.GONE);

                // 根据状态判断是否取消录制
                if (state == RecordButton.RecordState.READY_CANCEL) {
                    mRecordView.cancelRecord();
                } else if (state == RecordButton.RecordState.RECODEING) {
                    mRecordView.stopRecord();
                }
            }
        };
    }

    @TargetApi(Build.VERSION_CODES.M)
    private int getColorById(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            return ContextCompat.getColor(VideoRecorderActivity.this, id);
        } else {
            return getResources().getColor(id);
        }
    }
}
