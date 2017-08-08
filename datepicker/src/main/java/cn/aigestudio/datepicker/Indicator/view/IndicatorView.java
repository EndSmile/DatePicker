package cn.aigestudio.datepicker.Indicator.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.aigestudio.datepicker.bizs.themes.DPTManager;

/**
 * Created by ldy on 2016/11/5.
 */

public class IndicatorView extends LinearLayout {
    private TextView preView;
    private TextView currentView;
    private TextView postView;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
//        setBackgroundColor(Color.BLACK);
        init();
    }

    private void init() {
        preView = createView();
        currentView = createView();
        postView = createView();

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(preView, layoutParams);
        addView(currentView, layoutParams);
        addView(postView, layoutParams);
    }

    private TextView createView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setTextColor(DPTManager.getInstance().colorTitle());
        return textView;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh!=h){
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, h);
            if (preView==null){
                preView = createView();
                addView(preView,layoutParams);
            }else {
                preView.getLayoutParams().height = h;
            }
            if (currentView==null){
                currentView = createView();
                addView(currentView,layoutParams);
            }else {
                currentView.getLayoutParams().height = h;
            }
            if (postView==null){
                postView = createView();
                addView(postView,layoutParams);
            }else {
                postView.getLayoutParams().height = h;
            }
            requestLayout();
        }
    }

    public TextView getCurrentView() {
        return currentView;
    }

    public TextView getPreView() {
        return preView;
    }

    public TextView getPostView() {
        return postView;
    }
}
