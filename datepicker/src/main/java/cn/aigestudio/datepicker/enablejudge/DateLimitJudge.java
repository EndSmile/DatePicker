package cn.aigestudio.datepicker.enablejudge;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import cn.aigestudio.datepicker.entities.DateLimit;
import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2017/7/19.
 */

public class DateLimitJudge implements DayEnableJudge {
    private final Calendar limitStart;
    private final Calendar limitEnd;

    public DateLimitJudge(DateLimit dateLimit) {
        //使用大于，所以减一天
        limitStart =  new GregorianCalendar(dateLimit.start.year, dateLimit.start.month - 1, dateLimit.start.day);
        limitStart.add(Calendar.DAY_OF_MONTH,-1);


        //使用小于，所以加一天
        limitEnd =  new GregorianCalendar(dateLimit.end.year, dateLimit.end.month - 1, dateLimit.end.day);
        limitEnd.add(Calendar.DAY_OF_MONTH,1);
    }

    @Override
    public boolean isEnable(YMDDate ymdDate) {
        Calendar calendar = getCalendar(ymdDate);
        return calendar.after(limitStart) && calendar.before(limitEnd);
    }

    @Override
    public void disEnableClick(Context context) {

    }

    private static Map<YMDDate, Calendar> calendarCache = new HashMap<>();

    private static Calendar getCalendar(YMDDate ymdDate) {
        Calendar calendar = calendarCache.get(ymdDate);
        if (calendar == null) {
            if (calendarCache.size() > 150) {
                calendarCache.clear();
            }

            calendar = new GregorianCalendar(ymdDate.year, ymdDate.month - 1, ymdDate.day);
            calendarCache.put(ymdDate, calendar);
        }
        return calendar;
    }
}
