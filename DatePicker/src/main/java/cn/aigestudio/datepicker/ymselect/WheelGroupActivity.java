package cn.aigestudio.datepicker.ymselect;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.aigestudio.datepicker.R;
import cn.aigestudio.datepicker.wheeldate.WheelGroup;


public abstract class WheelGroupActivity extends AppCompatActivity {

    public static final String DATA = "data";
    private TextView tvCancel;
    private TextView tvCertain;
    protected WheelGroup wheelGroup;
    private FrameLayout flRoot;
    private View.OnClickListener clickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBack();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datepicker_activity_wheel_group);
        initView(savedInstanceState);
    }

    private void onBack() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBack();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void initView(@Nullable Bundle savedInstanceState) {
        tvCancel = (TextView) findViewById(R.id.tv_wheel_group_cancel);
        tvCertain = (TextView) findViewById(R.id.tv_wheel_group_certain);
        wheelGroup = (WheelGroup) findViewById(R.id.wp_wheel_group);
        flRoot = (FrameLayout) findViewById(R.id.fl_wheel_group_root);

        tvCancel.setOnClickListener(clickCancel);
        flRoot.setOnClickListener(clickCancel);

        tvCertain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                data.putExtra(DATA,wheelGroup.getCurrentValue());
                setResult(RESULT_OK, data);
                clickCancel.onClick(v);
            }
        });
    }


    public static void navigation(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityForResult(new Intent(activity, WheelGroupActivity.class), -1, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        }else {
            activity.startActivity(new Intent(activity,WheelGroupActivity.class));
        }
    }

    public static ArrayList<String> getData(Intent intent){
        return (ArrayList<String>) intent.getSerializableExtra(DATA);
    }
}
