package cn.aigestudio.datepicker.views;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.aigestudio.datepicker.Indicator.DateIndicator;
import cn.aigestudio.datepicker.Indicator.view.IndicatorView;
import cn.aigestudio.datepicker.R;
import cn.aigestudio.datepicker.entities.DateLimit;
import cn.aigestudio.datepicker.wheeldate.YearMouthDelegate;
import cn.aigestudio.datepicker.ymselect.WheelGroupActivity;
import cn.aigestudio.datepicker.ymselect.YearMonthWheelActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends Fragment {

    public static final int REQUEST_CODE = 1110;

    private ImageView ivLeftArrow;
    private IndicatorView indicator;
    private ImageView ivRightArrow;
    private MonthView monthView;

    public DatePickerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.datepicker_fragment_date_picker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivLeftArrow = (ImageView) view.findViewById(R.id.iv_datepicker_left_arrow);
        indicator = (IndicatorView) view.findViewById(R.id.indicator_datepicker);
        ivRightArrow = (ImageView) view.findViewById(R.id.iv_datepicker_right_arrow);
        monthView = ((MonthView) view.findViewById(R.id.monthView));
        monthView.setIndicator(new DateIndicator(indicator.getPreView(),indicator.getCurrentView(),indicator.getPostView()));
        ivLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthView.preDay();
            }
        });
        ivRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthView.postDay();
            }
        });
        view.findViewById(R.id.fl_datepicker_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateLimit limit = monthView.getDateLimit();
                if (limit==null){
                    limit = new DateLimit();
                }
                YearMouthDelegate.InitData initData =
                        new YearMouthDelegate.InitData(limit.startYear, limit.endYear,
                                limit.startMonth, limit.endMonth, monthView.getCenterYear(), monthView.getCenterMonth());
                YearMonthWheelActivity.navigation(DatePickerFragment.this, initData, REQUEST_CODE);
            }
        });
    }

    public MonthView getMonthView() {
        return monthView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<CharSequence> extra = data.getCharSequenceArrayListExtra(WheelGroupActivity.DATA);
            monthView.setDate(Integer.valueOf(extra.get(0).toString()), Integer.valueOf(extra.get(1).toString()));
        }
    }
}
