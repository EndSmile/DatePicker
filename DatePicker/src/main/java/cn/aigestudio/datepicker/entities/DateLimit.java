package cn.aigestudio.datepicker.entities;

/**
 * Created by ldy on 2017/7/12.
 */

public class DateLimit{
    public final int startYear, startMonth, endYear , endMonth;

    public DateLimit(int startYear, int startMonth, int endYear, int endMonth) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.endYear = endYear;
        this.endMonth = endMonth;
    }

    public DateLimit() {
        this(-2000,1,2999,12);
    }
}
