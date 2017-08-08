package cn.aigestudio.datepicker.enablejudge;

import android.content.Context;

import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2017/7/19.
 */

public interface DayEnableJudge {
    boolean isEnable(YMDDate ymdDate);

    /**
     * disEnable时点击的提示
     */
    void disEnableClick(Context context);
}
