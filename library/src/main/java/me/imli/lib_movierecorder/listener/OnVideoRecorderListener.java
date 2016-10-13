package me.imli.lib_movierecorder.listener;

import me.imli.lib_movierecorder.widget.VideoRecorderView;

/**
 * Created by Doots on 2016/10/10.
 */
public interface OnVideoRecorderListener {

    /**
     * 录制进度
     * @return
     */
    public void onRecorderProgress(VideoRecorderView view, int progress);

    /**
     * 录制完成
     * @param path
     */
    public void onComplete(VideoRecorderView view, String path, long recordTime);

    /**
     * 取消录制
     */
    public void onCancel(VideoRecorderView view);

}
