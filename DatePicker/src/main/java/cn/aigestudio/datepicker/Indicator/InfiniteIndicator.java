package cn.aigestudio.datepicker.Indicator;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by ldy on 2016/11/5.
 * 配合InfiniteViewPager使用的Indicator
 */
public abstract class InfiniteIndicator {
    protected View preView;
    protected View currentView;
    protected View postView;

    public InfiniteIndicator(View preView, View currentView, View postView) {
        this.preView = preView;
        this.currentView = currentView;
        this.postView = postView;
    }

    /**
     * InfiniteViewPager滑动时会调用这个函数
     * @param isRight   是否是向右滑动
     * @param positionOffset  see{@link ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)}:positionOffset
     * @param positionOffsetPixels see{@link ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)}:positionOffsetPixels
     */
    public abstract void onPageScrolled(boolean isRight,float positionOffset, int positionOffsetPixels);

    public View getPreView() {
        return preView;
    }

    public View getCurrentView() {
        return currentView;
    }

    public View getPostView() {
        return postView;
    }
}
