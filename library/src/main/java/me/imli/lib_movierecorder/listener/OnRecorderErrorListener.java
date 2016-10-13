package me.imli.lib_movierecorder.listener;

import android.media.MediaRecorder;

/**
 * Created by Doots on 2016/10/10.
 */
public class OnRecorderErrorListener implements MediaRecorder.OnErrorListener {
    @Override
    public void onError(MediaRecorder mediaRecorder, int i, int i1) {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
