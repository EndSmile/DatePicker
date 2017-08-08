package cn.aigestudio.datepicker.enablejudge;

import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2016/11/20.
 * 今天及今天以前返回true
 */
public class PreTomorrowEnableJudge extends PreDayEnableJudge {
    public PreTomorrowEnableJudge(int day, int month, int year) {
        super(day, month, year);
    }

    public PreTomorrowEnableJudge() {
    }

    @Override
    public boolean isEnable(YMDDate ymdDate) {
        if (this.year == ymdDate.year && this.month == ymdDate.month && this.day == ymdDate.day) {
            return true;
        }
        return super.isEnable(ymdDate);
    }

    @Override
    public String disEnableHint() {
        return "今天以后不可选择";
    }
}
