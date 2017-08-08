package cn.aigestudio.datepicker.Indicator;

import android.view.View;
import android.widget.TextView;

/**
 * Created by ldy on 2016/11/5.
 */

public class DateIndicator extends DefaultIndicator {
    public DateIndicator(View preView, View currentView, View postView) {
        super(preView, currentView, postView);
    }

    public void updateView(View view, String date) {
        ((TextView) view).setText(date);
    }

//    @Override
//    public void onPageScrolled(boolean isRight, float positionOffset, int positionOffsetPixels) {
//        float translationY = preView.getHeight() * positionOffset;
////        Log.d("ldy", "translationY:" + translationY + "\nisRight:" + isRight);
//        if (isRight) {
//            preView.setTranslationY(-preView.getHeight() * positionOffset);
//            currentView.setTranslationY(-currentView.getHeight() * positionOffset);
//            postView.setTranslationY(-postView.getHeight() * positionOffset);
//        } else {
//            preView.setTranslationY(preView.getHeight() * positionOffset);
//            currentView.setTranslationY(currentView.getHeight() * positionOffset);
//            postView.setTranslationY(postView.getHeight() * positionOffset);
//        }
//    }

    public String getDateStr(int year, int month){
        return year+"-"+month;
    }
}
