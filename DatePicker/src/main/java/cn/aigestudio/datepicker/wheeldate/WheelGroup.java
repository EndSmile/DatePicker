package cn.aigestudio.datepicker.wheeldate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldy on 2016/11/4.
 */

public class WheelGroup extends LinearLayout {

    private List<WheelView> wheelViewList;

    public WheelGroup(Context context) {
        this(context, null);
    }

    public WheelGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
    }

    public void init(WheelGroupDelegate wheelGroupDelegate) {
        wheelViewList = wheelGroupDelegate.getWheelViewList();
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        for (int i = 0; i < wheelViewList.size(); i++) {
            WheelView wheelView = wheelViewList.get(i);
            addView(wheelView,layoutParams);
//            LayoutInflater.from(getContext()).inflate(R.layout.view_wheel,this);
        }
    }

    public ArrayList<String> getCurrentValue() {
        ArrayList<String> strings = new ArrayList<>();
        for (WheelView wheelView : wheelViewList) {
            strings.add(wheelView.getSelectedText());
        }
        return strings;
    }
}
