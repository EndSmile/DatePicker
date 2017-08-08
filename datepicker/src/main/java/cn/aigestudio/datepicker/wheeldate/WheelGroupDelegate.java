package cn.aigestudio.datepicker.wheeldate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;


import java.util.ArrayList;
import java.util.List;

import cn.aigestudio.datepicker.R;

/**
 * Created by ldy on 2016/11/4.
 */

public abstract class WheelGroupDelegate {

    private final Context context;

    public WheelGroupDelegate(Context context){
        this.context = context;
    }

    @NonNull
    public abstract List<WheelView> getWheelViewList();

    protected WheelView createWheelView(){
        return (WheelView) LayoutInflater.from(context).inflate(R.layout.datepicker_view_wheel,null);
    }

    protected ArrayList<String> getListFromInt(int start, int end) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }
}
