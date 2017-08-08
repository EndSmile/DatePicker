package cn.aigestudio.datepicker.enablejudge;

import android.content.Context;
import android.widget.Toast;

import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2017/7/19.
 */

public abstract class ToastHintJudge implements DayEnableJudge {
    @Override
    public boolean isEnable(YMDDate ymdDate) {
        return false;
    }

    @Override
    public void disEnableClick(Context context) {
        Toast.makeText(context,disEnableHint(),Toast.LENGTH_SHORT).show();
    }

    protected abstract String disEnableHint();
}
