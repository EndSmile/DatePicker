package cn.aigestudio.datepicker.entities;

/**
 * Created by ldy on 2017/7/12.
 */

public class DateLimit{
    public final YMDDate start,end;
//    public final int startYear, startMonth, endYear , endMonth;
//
//    public DateLimit(int startYear, int startMonth, int endYear, int endMonth) {
//        this.startYear = startYear;
//        this.startMonth = startMonth;
//        this.endYear = endYear;
//        this.endMonth = endMonth;
//    }
    public DateLimit(YMDDate start,YMDDate end){
        this.start = start;
        this.end = end;
    }

    public DateLimit() {
        this(new YMDDate(2000,1,1),new YMDDate(2099,12,31));
    }
}
