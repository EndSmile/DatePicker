package cn.aigestudio.datepicker.entities;

/**
 * Created by ldy on 2017/7/19.
 */

public class YMDDate {
    public final int year, month, day;

    public YMDDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }


    public static YMDDate buildByYyyy_M_d(String date) {
        String[] strings = date.split("-");
        return new YMDDate(Integer.valueOf(strings[0]),
                Integer.valueOf(strings[1]),
                Integer.valueOf(strings[2]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YMDDate ymdDate = (YMDDate) o;

        if (year != ymdDate.year) return false;
        if (month != ymdDate.month) return false;
        return day == ymdDate.day;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }
}
