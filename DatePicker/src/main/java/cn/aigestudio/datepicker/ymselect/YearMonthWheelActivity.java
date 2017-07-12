package cn.aigestudio.datepicker.ymselect;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import cn.aigestudio.datepicker.wheeldate.YearMouthDelegate;


/**
 * Created by ldy on 2016/11/10.
 */

public class YearMonthWheelActivity extends WheelGroupActivity {

    public static final String INIT_DATA = "loadData";

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        YearMouthDelegate yearMouthDelegate = new YearMouthDelegate(this);
        yearMouthDelegate.setInitData((YearMouthDelegate.InitData) getIntent().getParcelableExtra(INIT_DATA));
        wheelGroup.init(yearMouthDelegate);
    }

    public static void navigation(Activity activity, YearMouthDelegate.InitData initData, int requestCode) {
        Intent intent = new Intent(activity, YearMonthWheelActivity.class);
        intent.putExtra(INIT_DATA,initData);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivityForResult(intent,requestCode);
        }
    }
    public static void navigation(Fragment fragment, YearMouthDelegate.InitData initData, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), YearMonthWheelActivity.class);
        intent.putExtra(INIT_DATA,initData);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity()).toBundle());
        } else {
            fragment.startActivityForResult(intent,requestCode);
        }
    }

}
