package cn.aigestudio.datepicker.Indicator;

import android.view.View;

/**
 * Created by ldy on 2016/11/5.
 */

public class MockIndicator extends InfiniteIndicator {
    public MockIndicator() {
        super(null, null, null);
    }

    public MockIndicator(View preView, View currentView, View postView) {
        super(preView, currentView, postView);
    }

    @Override
    public void onPageScrolled(boolean isRight, float positionOffset, int positionOffsetPixels) {

    }
}
