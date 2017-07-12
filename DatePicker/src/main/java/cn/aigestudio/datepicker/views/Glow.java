package cn.aigestudio.datepicker.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by ldy on 2017/7/12.
 */

public class Glow {
    private EdgeEffectCompat mLeftGlow;
    private EdgeEffectCompat mRightGlow;
    private View view;
    private Paint paint;

    public Glow(View view) {
        this.view = view;
    }

    public void onDraw(Canvas canvas) {

        if (mLeftGlow != null) {
            if (!mLeftGlow.isFinished()) {
                int restoreCount = canvas.save();
                canvas.rotate(270);
                canvas.translate(-view.getHeight(), 0);
                if (mLeftGlow.draw(canvas)) {//绘制边缘效果图，如果绘制需要进行动画效果返回true
                    Log.d("Glow", "leftDraw");
                    ViewCompat.postInvalidateOnAnimation(view);//进行动画
                }
                canvas.restoreToCount(restoreCount);
            }
        }
        if (mRightGlow != null) {
            if (!mRightGlow.isFinished()) {
                int restoreCount = canvas.save();
                //下面两行代码的作用就是把画布平移旋转到底部展示，并让效果向上显示
                canvas.rotate(90);
                canvas.translate(0, -view.getWidth());
                if (mRightGlow.draw(canvas)) {//绘制边缘效果图，如果绘制需要进行动画效果返回true
                    ViewCompat.postInvalidateOnAnimation(view);//进行动画
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    public void leftOnPull(float deltaDistance, float displacement) {
        ensureLeftGlow();
        mLeftGlow.onPull(deltaDistance, displacement);
        if (mLeftGlow != null && (!mLeftGlow.isFinished())) {
            ViewCompat.postInvalidateOnAnimation(view);
        }
    }

    public void rightOnPull(float deltaDistance, float displacement) {
        ensureRightGlow();
        //由于翻转180度显示，所以X轴坐标需要以中心翻转
        mRightGlow.onPull(deltaDistance, displacement);

        Log.d("Glow", "deltaDistance:" + deltaDistance);
        Log.d("Glow", "displacement:" + displacement);

        if (mRightGlow != null && (!mRightGlow.isFinished())) {
            ViewCompat.postInvalidateOnAnimation(view);
        }
    }

    public void release(){
        if (mLeftGlow!=null){
            mLeftGlow.onRelease();
        }
        if (mRightGlow!=null){
            mRightGlow.onRelease();
        }
    }

    private void ensureLeftGlow() {
        if (mLeftGlow != null) {
            return;
        }
        mLeftGlow = new EdgeEffectCompat(view.getContext());
        mLeftGlow.setSize(view.getWidth(), view.getHeight());
    }

    private void ensureRightGlow() {
        if (mRightGlow != null) {
            return;
        }
        mRightGlow = new EdgeEffectCompat(view.getContext());
        mRightGlow.setSize(view.getWidth(), view.getHeight());
    }
}
