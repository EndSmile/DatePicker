package cn.aigestudio.datepicker.wheeldate;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by ldy on 2016/11/4.
 */

public class YearMouthDelegate extends WheelGroupDelegate {

    private InitData initData;

    public YearMouthDelegate(Context context) {
        super(context);
    }


    public void setInitData(InitData initData) {
        this.initData = initData;
    }

    @NonNull
    @Override
    public List<WheelView> getWheelViewList() {
        WheelView yearView = createWheelView();
        final WheelView monthView = createWheelView();
        initYearView(yearView, monthView);
        initMonthView(initData.centerYear, monthView);
        return Arrays.asList(yearView, monthView);
    }

    private void initYearView(WheelView yearView, final WheelView monthView) {
        yearView.setData(getListFromInt(initData.startYear, initData.endYear));
        yearView.setDefault(initData.centerYear - initData.startYear);
        yearView.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, final String text) {
                monthView.post(new Runnable() {
                    @Override
                    public void run() {
                        Integer year = Integer.valueOf(text);
                        initMonthView(year, monthView);
                        monthView.invalidate();
                    }
                });

            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

    private void initMonthView(int year, WheelView monthView) {
        if (year == initData.startYear && year == initData.endYear) {
            monthView.setData(getListFromInt(initData.startMonth, initData.endMonth));
            monthView.setDefault(initData.centerMonth - 1);
        } else if (year == initData.endYear) {
            monthView.setData(getListFromInt(1, initData.endMonth));
            monthView.setDefault(initData.endMonth - 1);
        } else if (year == initData.startYear) {
            monthView.setData(getListFromInt(initData.startMonth, 12));
            monthView.setDefault(initData.startMonth - 1);
        } else if (year == initData.centerYear) {
            monthView.setData(getListFromInt(1, 12));
            monthView.setDefault(initData.centerMonth - 1);
        } else {
            monthView.setData(getListFromInt(1, 12));
            monthView.setDefault(2);
        }
    }


    public static class InitData implements Parcelable {

        public int startYear;
        public int endYear;
        public int centerYear;
        public int startMonth;
        public int endMonth;
        public int centerMonth;

        public InitData() {
        }

        public InitData(int startYear, int endYear, int startMonth, int endMonth, int centerYear, int centerMonth) {
            this.startYear = startYear;
            this.endYear = endYear;
            this.startMonth = startMonth;
            this.centerYear = centerYear;
            this.endMonth = endMonth;
            this.centerMonth = centerMonth;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.startYear);
            dest.writeInt(this.endYear);
            dest.writeInt(this.centerYear);
            dest.writeInt(this.startMonth);
            dest.writeInt(this.endMonth);
            dest.writeInt(this.centerMonth);
        }

        public InitData(Parcel in) {
            this.startYear = in.readInt();
            this.endYear = in.readInt();
            this.centerYear = in.readInt();
            this.startMonth = in.readInt();
            this.endMonth = in.readInt();
            this.centerMonth = in.readInt();
        }

        public static final Parcelable.Creator<InitData> CREATOR = new Parcelable.Creator<InitData>() {
            @Override
            public InitData createFromParcel(Parcel source) {
                return new InitData(source);
            }

            @Override
            public InitData[] newArray(int size) {
                return new InitData[size];
            }
        };
    }
}
