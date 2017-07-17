package cn.aigestudio.datepicker.demo;

import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import cn.aigestudio.datepicker.views.DatePickerFragment;
import cn.aigestudio.datepicker.views.MonthView;

public class DatePickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);
//        final DatePickerFragment fragment = new DatePickerFragment();
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, fragment)
//                .commitNow();
//        getWindow().getDecorView().post(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        MonthView monthView = fragment.getMonthView();
//                        monthView.setDate(2017,10);
//                    }
//                }
//        );

        DatePickerFragment fragment = (DatePickerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_datepicker);
        MonthView monthView = fragment.getMonthView();
        monthView.setDate(2017, 10);
    }
}
