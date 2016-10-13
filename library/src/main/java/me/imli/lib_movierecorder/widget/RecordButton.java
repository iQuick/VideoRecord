package me.imli.lib_movierecorder.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import me.imli.lib_movierecorder.utils.DensityUtil;

/**
 * Created by Doots on 2016/10/10.
 */
public class RecordButton extends Button {

    // 按钮状态
    private RecordState mCurState = RecordState.NORMAL;

    // 监听器
    private OnTouchRecordListener mOnTouchRecordListener = null;

    public RecordButton(Context context) {
        this(context, null);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置监听器
     * @param listener
     */
    public void setOnTouchRecordListener(OnTouchRecordListener listener) {
        mOnTouchRecordListener = listener;
    }

    private float startY = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnTouchRecordListener != null) {
                    mOnTouchRecordListener.onDown(this);
                }
                // 按下时坐标
                startY = event.getY();
                mCurState = RecordState.RECODEING;
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动时的行动 Y 轴差
                float difY = startY - event.getY();
                if (difY >= DensityUtil.dip2px(getContext(), 50) && mCurState == RecordState.RECODEING) {
                    mCurState = RecordState.READY_CANCEL;
                    if (mOnTouchRecordListener != null) {
                        mOnTouchRecordListener.onStateChange(this, mCurState);
                    }
                } else if (difY <= DensityUtil.dip2px(getContext(), 20) && mCurState == RecordState.READY_CANCEL){
                    mCurState = RecordState.RECODEING;
                    if (mOnTouchRecordListener != null) {
                        mOnTouchRecordListener.onStateChange(this, mCurState);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mOnTouchRecordListener != null) {
                    mOnTouchRecordListener.onUp(this, mCurState);
                }
                // 恢复正常状态
                mCurState = RecordState.NORMAL;
                break;
        }
        return true;
    }

    /**
     * 按钮录制状态
     */
    public enum RecordState {
        NORMAL,         // 正常
        RECODEING,      // 正在录制
        READY_CANCEL    // 准备取消
    }

    /**
     * OnRecordStateChangeListener
     */
    public interface OnTouchRecordListener {
        /**
         * 状态改变
         * @param state
         */
        public void onStateChange(RecordButton btn, RecordState state);

        /**
         * 按下
         * @param btn
         */
        public void onDown(RecordButton btn);

        /**
         * 释放
         * @param state
         */
        public void onUp(RecordButton btn, RecordState state);
    }
}
