package cn.aigestudio.datepicker.enablejudge;

import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2016/11/20.
 * 今天及以后返回true
 */
public class PostDayEnableJudge extends PreDayEnableJudge{
    public PostDayEnableJudge(int day, int month, int year) {
        super(day, month, year);
    }

    public PostDayEnableJudge() {
    }

    @Override
    public boolean isEnable(YMDDate ymdDate) {
        return !super.isEnable(ymdDate);
    }

    @Override
    public String disEnableHint() {
        return "今天以前不可选择";
    }
}
