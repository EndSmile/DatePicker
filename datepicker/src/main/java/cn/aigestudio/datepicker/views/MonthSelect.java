package cn.aigestudio.datepicker.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.text.TextUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.enablejudge.DayEnableJudge;
import cn.aigestudio.datepicker.entities.DPInfo;
import cn.aigestudio.datepicker.entities.YMDDate;

/**
 * Created by ldy on 2017/7/12.
 * 将monthview点击的处理拆分到这个类里
 */

public class MonthSelect {
    private Map<String, BGCircle> cirApr = new HashMap<>();
    private Map<String, BGCircle> cirDpr = new HashMap<>();

    private List<String> dateSelected = new ArrayList<>();

    private int animZoomOut1, animZoomIn1, animZoomOut2;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    private ScaleAnimationListener scaleAnimationListener;
    private DatePicker.OnDatePickedListener onDatePickedListener;


    private DPMode mDPMode = DPMode.MULTIPLE;
    private MonthView monthView;
    private DayEnableJudge dayEnableJudge;


    MonthSelect(MonthView monthView) {
        this.monthView = monthView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            scaleAnimationListener = new ScaleAnimationListener();
        }
    }

    public void onSizeChange(int cellW) {
        animZoomOut1 = (int) (cellW * 1.2F);
        animZoomIn1 = (int) (cellW * 0.8F);
        animZoomOut2 = (int) (cellW * 1.1F);
    }

    public void defineRegion(int x, int y) {
        DPInfo[][] info = monthView.mCManager.obtainDPInfo(monthView.centerYear, monthView.centerMonth);
        Region[][] tmp;
        if (TextUtils.isEmpty(info[4][0].strG)) {
            tmp = monthView.MONTH_REGIONS_4;
        } else if (TextUtils.isEmpty(info[5][0].strG)) {
            tmp = monthView.MONTH_REGIONS_5;
        } else {
            tmp = monthView.MONTH_REGIONS_6;
        }
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                Region region = tmp[i][j];
                DPInfo[][] dpInfos = monthView.mCManager.obtainDPInfo(monthView.centerYear, monthView.centerMonth);
                if (TextUtils.isEmpty(dpInfos[i][j].strG)) {
                    continue;
                }
                if (region.contains(x, y)) {
                    if (mDPMode != DPMode.NONE) {
                        if (dayDisEnableJudge(dpInfos[i][j])) continue;
                    }

                    if (mDPMode == DPMode.SINGLE) {
                        cirApr.clear();
                        final String date = monthView.centerYear + "-" + monthView.centerMonth + "-" +
                                dpInfos[i][j].strG;
                        BGCircle circle = createCircle(
                                region.getBounds().centerX() + monthView.indexMonth * monthView.width,
                                region.getBounds().centerY() + monthView.indexYear * monthView.height);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            ValueAnimator animScale1 =
                                    ObjectAnimator.ofInt(circle, "radius", 0, animZoomOut1);
                            animScale1.setDuration(250);
                            animScale1.setInterpolator(decelerateInterpolator);
                            animScale1.addUpdateListener(scaleAnimationListener);

                            ValueAnimator animScale2 =
                                    ObjectAnimator.ofInt(circle, "radius", animZoomOut1, animZoomIn1);
                            animScale2.setDuration(100);
                            animScale2.setInterpolator(accelerateInterpolator);
                            animScale2.addUpdateListener(scaleAnimationListener);

                            ValueAnimator animScale3 =
                                    ObjectAnimator.ofInt(circle, "radius", animZoomIn1, animZoomOut2);
                            animScale3.setDuration(150);
                            animScale3.setInterpolator(decelerateInterpolator);
                            animScale3.addUpdateListener(scaleAnimationListener);

                            ValueAnimator animScale4 =
                                    ObjectAnimator.ofInt(circle, "radius", animZoomOut2, monthView.circleRadius);
                            animScale4.setDuration(50);
                            animScale4.setInterpolator(accelerateInterpolator);
                            animScale4.addUpdateListener(scaleAnimationListener);

                            AnimatorSet animSet = new AnimatorSet();
                            animSet.playSequentially(animScale1, animScale2, animScale3, animScale4);
                            animSet.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    if (null != onDatePickedListener) {
                                        onDatePickedListener.onDatePicked(date);
                                    }
                                }
                            });
                            animSet.start();
                        }
                        cirApr.put(date, circle);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            monthView.invalidate();
                            if (null != onDatePickedListener) {
                                onDatePickedListener.onDatePicked(date);
                            }
                        }
                    } else if (mDPMode == DPMode.SINGLE_NO_ANIM) {
                        if (null != onDatePickedListener) {
                            final String date = monthView.centerYear + "-" + monthView.centerMonth + "-" +
                                    dpInfos[i][j].strG;
                            onDatePickedListener.onDatePicked(date);
                        }
                    } else if (mDPMode == DPMode.MULTIPLE) {
                        final String date = monthView.centerYear + "-" + monthView.centerMonth + "-" +
                                dpInfos[i][j].strG;
                        if (dateSelected.contains(date)) {
                            dateSelected.remove(date);
                            BGCircle circle = cirApr.get(date);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                ValueAnimator animScale = ObjectAnimator.ofInt(circle, "radius", monthView.circleRadius, 0);
                                animScale.setDuration(250);
                                animScale.setInterpolator(accelerateInterpolator);
                                animScale.addUpdateListener(scaleAnimationListener);
                                animScale.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        cirDpr.remove(date);
                                    }
                                });
                                animScale.start();
                                cirDpr.put(date, circle);
                            }
                            cirApr.remove(date);
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                monthView.invalidate();
                            }
                        } else {
                            dateSelected.add(date);
                            BGCircle circle = createCircle(
                                    region.getBounds().centerX() + monthView.indexMonth * monthView.width,
                                    region.getBounds().centerY() + monthView.indexYear * monthView.height);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                ValueAnimator animScale1 =
                                        ObjectAnimator.ofInt(circle, "radius", 0, animZoomOut1);
                                animScale1.setDuration(250);
                                animScale1.setInterpolator(decelerateInterpolator);
                                animScale1.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale2 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomOut1, animZoomIn1);
                                animScale2.setDuration(100);
                                animScale2.setInterpolator(accelerateInterpolator);
                                animScale2.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale3 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomIn1, animZoomOut2);
                                animScale3.setDuration(150);
                                animScale3.setInterpolator(decelerateInterpolator);
                                animScale3.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale4 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomOut2, monthView.circleRadius);
                                animScale4.setDuration(50);
                                animScale4.setInterpolator(accelerateInterpolator);
                                animScale4.addUpdateListener(scaleAnimationListener);

                                AnimatorSet animSet = new AnimatorSet();
                                animSet.playSequentially(animScale1, animScale2, animScale3, animScale4);
                                animSet.start();
                            }
                            cirApr.put(date, circle);
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                monthView.invalidate();
                            }
                        }
                    } else if (mDPMode == DPMode.NONE) {
                        final String date = monthView.centerYear + "-" + monthView.centerMonth + "-" +
                                dpInfos[i][j].strG;
                        if (dateSelected.contains(date)) {
                            dateSelected.remove(date);
                        } else {
                            dateSelected.add(date);
                        }
                    }
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for (String s : cirDpr.keySet()) {
                BGCircle circle = cirDpr.get(s);
                drawBGCircle(canvas, circle);
            }
        }
        for (String s : cirApr.keySet()) {
            BGCircle circle = cirApr.get(s);
            drawBGCircle(canvas, circle);
        }
    }

    private void drawBGCircle(Canvas canvas, BGCircle circle) {
        canvas.save();
        canvas.translate(circle.getX() - circle.getRadius() / 2,
                circle.getY() - circle.getRadius() / 2);
        circle.getShape().getShape().resize(circle.getRadius(), circle.getRadius());
        circle.getShape().draw(canvas);
        canvas.restore();
    }

    public void release() {
        cirApr.clear();
        cirDpr.clear();
        dateSelected.clear();
    }


    private BGCircle createCircle(float x, float y) {
        OvalShape circle = new OvalShape();
        circle.resize(0, 0);
        ShapeDrawable drawable = new ShapeDrawable(circle);
        BGCircle circle1 = new BGCircle(drawable);
        circle1.setX(x);
        circle1.setY(y);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            circle1.setRadius(monthView.circleRadius);
        }
        drawable.getPaint().setColor(monthView.mTManager.colorBGCircle());
        return circle1;
    }

    private boolean dayDisEnableJudge(DPInfo dpInfo) {
        if (dayEnableJudge == null) {
            return false;
        }
        if (!dayEnableJudge.isEnable(new YMDDate(monthView.centerYear, monthView.centerMonth, Integer.valueOf(dpInfo.strG)))) {
            dayEnableJudge.disEnableClick(monthView.getContext());
            return true;
        }
        return false;
    }

    public void setOnDatePickedListener(DatePicker.OnDatePickedListener onDatePickedListener) {
        this.onDatePickedListener = onDatePickedListener;
    }

    public List<String> getDateSelected() {
        return dateSelected;
    }

    public void setDPMode(DPMode DPMode) {
        this.mDPMode = DPMode;
    }

    public DPMode getDPMode() {
        return mDPMode;
    }

    public void setDayEnableJudge(DayEnableJudge dayEnableJudge) {
        this.dayEnableJudge = dayEnableJudge;
    }

    private class BGCircle {
        private float x, y;
        private int radius;

        private ShapeDrawable shape;

        public BGCircle(ShapeDrawable shape) {
            this.shape = shape;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public ShapeDrawable getShape() {
            return shape;
        }

        public void setShape(ShapeDrawable shape) {
            this.shape = shape;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class ScaleAnimationListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            monthView.invalidate();
        }
    }

}
