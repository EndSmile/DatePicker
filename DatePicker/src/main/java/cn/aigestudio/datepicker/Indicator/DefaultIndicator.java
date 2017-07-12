package cn.aigestudio.datepicker.Indicator;

import android.view.View;

/**
 * Created by ldy on 2016/11/5.
 */

public class DefaultIndicator extends InfiniteIndicator {
    public DefaultIndicator(View preView, View currentView, View postView) {
        super(preView, currentView, postView);
    }

    @Override
    public void onPageScrolled(boolean isRight, float positionOffset, int positionOffsetPixels) {

        if (isRight) {
            preView.setTranslationY(-preView.getHeight() * positionOffset);
            currentView.setTranslationY(-currentView.getHeight() * positionOffset);
            postView.setTranslationY(-postView.getHeight() * positionOffset);
        } else {
            float translationY = preView.getHeight() * (1 - positionOffset);
            preView.setTranslationY(translationY);
            currentView.setTranslationY(currentView.getHeight() * (1 - positionOffset));
            postView.setTranslationY(postView.getHeight() * (1 - positionOffset));
        }
    }
}
