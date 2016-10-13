package me.imli.lib_movierecorder.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Doots on 2016/10/11.
 */
public class VideoPreviewView extends VideoView {

    // 视频播放监听
    private OnVideoPlayListener mOnVideoPlayListener;
    private OnVideoBufferListener mOnVideoBufferListener;
    // Handler
    private Handler mHandler;
    private Timer mTimer;

    public VideoPreviewView(Context context) {
        this(context, null);
    }

    public VideoPreviewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        mTimer = new Timer();
        mHandler = new Handler(Looper.getMainLooper());
        setOnPreparedListener(onPreparedListener());
    }

    /**
     * 设置视频播放监听
     * @param listener
     */
    public void setOnVideoPlayListener(OnVideoPlayListener listener) {
        mOnVideoPlayListener = listener;
    }

    /**
     * 设置视频缓冲监听
     * @param listener
     */
    public void setOnVideoBufferListener(OnVideoBufferListener listener) {
        mOnVideoBufferListener = listener;
    }


    /**
     * 计时
     * @return
     */
    private TimerTask timerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                final int curPosition = getCurrentPosition() * 100;
                final int duration = getDuration();

                if (mOnVideoPlayListener != null) {
                    mOnVideoPlayListener.onProgress(VideoPreviewView.this, curPosition, duration);
                }

            }
        };
    }

    /**
     * 视频监听
     * @return
     */
    private MediaPlayer.OnPreparedListener onPreparedListener() {
        return new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
                mediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener());
            }
        };
    }

    /**
     * 缓冲改变监听
     * @return
     */
    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener() {
        return new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, final int i) {

                if (mOnVideoBufferListener != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnVideoBufferListener.onBuffering(VideoPreviewView.this, i);
                        }
                    });
                }
            }
        };
    }

    @Override
    public void start() {
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(timerTask(), 0, 1000);
        super.start();
    }

    @Override
    public void stopPlayback() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        super.stopPlayback();
    }


    /**
     * 缓冲监听
     */
    public interface OnVideoBufferListener {

        /**
         * 缓冲进度
         * @param progress
         */
        public void onBuffering(VideoPreviewView view, int progress);

    }

    /**
     * 播放监听
     */
    public interface OnVideoPlayListener {

        /**
         *
         * @param play  播放进度
         * @param duration  持续时间
         */
        public void onProgress(VideoPreviewView view, int play, int duration);

    }

}
