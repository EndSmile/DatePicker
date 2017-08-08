package cn.aigestudio.datepicker.enablejudge;



import java.util.Calendar;

import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2016/11/8.
 * 今天以前返回true
 */
public class PreDayEnableJudge extends ToastHintJudge {

    protected int year;
    protected int month;
    protected int day;

    public PreDayEnableJudge(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public PreDayEnableJudge() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public boolean isEnable(YMDDate ymdDate) {
        if (ymdDate.year > this.year) {
            return false;
        }
        if (ymdDate.year == this.year) {
            if (ymdDate.month > this.month) {
                return false;
            }
            if (ymdDate.month == this.month) {
                if (ymdDate.day >= this.day) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String disEnableHint() {
        return "今天及今天以后不可选择";
    }

}
