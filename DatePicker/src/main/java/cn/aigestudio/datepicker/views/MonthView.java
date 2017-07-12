package cn.aigestudio.datepicker.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.util.Arrays;
import java.util.List;

import cn.aigestudio.datepicker.Indicator.DateIndicator;
import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.bizs.themes.DPTManager;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.entities.DPInfo;
import cn.aigestudio.datepicker.entities.DateLimit;

/**
 * MonthView
 *
 * @author AigeStudio 2015-06-29
 */
public class MonthView extends View {
    final Region[][] MONTH_REGIONS_4 = new Region[4][7];
    final Region[][] MONTH_REGIONS_5 = new Region[5][7];
    final Region[][] MONTH_REGIONS_6 = new Region[6][7];

    private final DPInfo[][] INFO_4 = new DPInfo[4][7];
    private final DPInfo[][] INFO_5 = new DPInfo[5][7];
    private final DPInfo[][] INFO_6 = new DPInfo[6][7];
    private final MonthSelect monthSelect;
    private final Glow glow;


    DPCManager mCManager = new DPCManager();
    DPTManager mTManager = DPTManager.getInstance();

    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG |
            Paint.LINEAR_TEXT_FLAG);
    private Scroller mScroller;
    private OnDateChangeListener onDateChangeListener;

    private SlideMode mSlideMode;
    private DPDecor mDPDecor;

    int circleRadius;
    int indexYear;
    int indexMonth;
    int centerYear;
    int centerMonth;
    private int leftYear, leftMonth;
    private int rightYear, rightMonth;
    private int topYear, topMonth;
    private int bottomYear, bottomMonth;
    private int width, height;
    private int sizeDecor, sizeDecor2x, sizeDecor3x;
    private int lastPointX, lastPointY;
    private int lastMoveX, lastMoveY;
    private int criticalWidth, criticalHeight;

    private float sizeTextGregorian, sizeTextFestival;
    private float offsetYFestival1, offsetYFestival2;

    private boolean isNewEvent,
            isFestivalDisplay = true,
            isHolidayDisplay = true,
            isTodayDisplay = true,
            isDeferredDisplay = true;
    private boolean isAllowVerScroll = true;

    private DateLimit dateLimit;
    private boolean isRightScrolling;
    private DateIndicator indicator;

    public MonthView(Context context) {
        this(context,null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mPaint.setTextAlign(Paint.Align.CENTER);
        monthSelect = new MonthSelect(this);
        glow = new Glow(this);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    private float lastScrollMoveX = -1;
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();

            //移动指示器
            if (indicator != null) {
                float moveX = Math.abs(mScroller.getCurrX()) % width;
                if (moveX != lastScrollMoveX) {
                    if ((isRightScrolling && mScroller.getCurrX() < 0)) {
                        moveX = width - moveX;
                    }
                    if (!isRightScrolling && mScroller.getCurrX() > 0) {
                        moveX = width - moveX;
                    }
                    lastScrollMoveX = moveX;
                    if (moveX == 0 || moveX == width) {
                        indicator.updateView(indicator.getCurrentView(), indicator.getDateStr(centerYear, centerMonth));
                        post(new Runnable() {
                            @Override
                            public void run() {
                                indicator.onPageScrolled(true, 0, 0);
                                indicator.updateView(indicator.getPreView(), getPreMonthDate(indicator));
                                indicator.updateView(indicator.getPostView(), getPostMonthDate(indicator));
                            }
                        });

                    } else {
                        if (!isRightScrolling)
                            indicator.onPageScrolled(isRightScrolling, 1 - moveX / width, (int) (width - moveX));
                        else
                            indicator.onPageScrolled(isRightScrolling, moveX / width, (int) moveX);
                    }
                }
            }
        } else {
            requestLayout();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mSlideMode = null;
                isNewEvent = true;
                lastPointX = (int) event.getX();
                lastPointY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX() - lastPointX;
                isRightScrolling = moveX < 0;
                if (isNewEvent) {
                    if (Math.abs(lastPointX - event.getX()) > 100) {
                        mSlideMode = SlideMode.HOR;
                        isNewEvent = false;
                    } else if (Math.abs(lastPointY - event.getY()) > 50 && isAllowVerScroll) {
                        mSlideMode = SlideMode.VER;
                        isNewEvent = false;
                    }
                }
                if (mSlideMode == SlideMode.HOR) {
                    if (isInterruptHorScroller(moveX)) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        if (moveX>0){
                            glow.leftOnPull(Math.abs(moveX) / getWidth(), 1-event.getY() / getHeight());
                        }else {
                            glow.rightOnPull(Math.abs(moveX) / getWidth(), event.getY() / getHeight());
                        }
                        return false;
                    }
                    int totalMoveX = (int) (lastPointX - event.getX()) + lastMoveX;
                    smoothScrollTo(totalMoveX, indexYear * height);
                } else if (mSlideMode == SlideMode.VER) {
                    int totalMoveY = (int) (lastPointY - event.getY()) + lastMoveY;
                    smoothScrollTo(width * indexMonth, totalMoveY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSlideMode == SlideMode.VER) {
                    if (Math.abs(lastPointY - event.getY()) > 25) {
                        if (lastPointY < event.getY()) {
                            if (Math.abs(lastPointY - event.getY()) >= criticalHeight) {
                                indexYear--;
                                centerYear = centerYear - 1;
                            }
                        } else if (lastPointY > event.getY()) {
                            if (Math.abs(lastPointY - event.getY()) >= criticalHeight) {
                                indexYear++;
                                centerYear = centerYear + 1;
                            }
                        }
                        computeDate();
                        smoothScrollTo(width * indexMonth, height * indexYear);
                        lastMoveY = height * indexYear;
                    } else {
                        monthSelect.defineRegion((int) event.getX(), (int) event.getY());
                    }
                } else if (mSlideMode == SlideMode.HOR) {
                    moveX = event.getX() - lastPointX;
                    if (isInterruptHorScroller(moveX)) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                        glow.release();
                        return false;
                    }
                    if (Math.abs(lastPointX - event.getX()) > 25) {
                        if (lastPointX > event.getX() &&
                                Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            indexMonth++;
                            centerMonth = (centerMonth + 1) % 13;
                            if (centerMonth == 0) {
                                centerMonth = 1;
                                centerYear++;
                            }
                        } else if (lastPointX < event.getX() &&
                                Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            indexMonth--;
                            centerMonth = (centerMonth - 1) % 12;
                            if (centerMonth == 0) {
                                centerMonth = 12;
                                centerYear--;
                            }
                        }
                        computeDate();
                        smoothScrollTo(width * indexMonth, indexYear * height);
                        lastMoveX = width * indexMonth;
                    } else {
                        monthSelect.defineRegion((int) event.getX(), (int) event.getY());
                    }
                } else {
                    monthSelect.defineRegion((int) event.getX(), (int) event.getY());
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                glow.release();
                break;
        }
        return true;
    }

    public void preDay() {
        if (isStartDate()) {
            return;
        }

        isRightScrolling = false;

        indexMonth--;
        centerMonth = (centerMonth - 1) % 12;
        if (centerMonth == 0) {
            centerMonth = 12;
            centerYear--;
        }
        computeDate();
        smoothScrollTo(width * indexMonth, indexYear * height);
        lastMoveX = width * indexMonth;

    }

    public void postDay() {
        if (isEndDate()) {
            return;
        }

        isRightScrolling = true;

        indexMonth++;
        centerMonth = (centerMonth + 1) % 13;
        if (centerMonth == 0) {
            centerMonth = 1;
            centerYear++;
        }
        computeDate();
        smoothScrollTo(width * indexMonth, indexYear * height);
        lastMoveX = width * indexMonth;
    }

    private boolean isInterruptHorScroller(float moveX) {
        if (moveX > 0) {
            if (isStartDate()) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return true;
            }
        } else {
            if (isEndDate()) {
                getParent().requestDisallowInterceptTouchEvent(false);
                return true;
            }
        }
        return false;
    }

    private boolean isStartDate() {
        return dateLimit != null && centerYear == dateLimit.startYear && centerMonth == dateLimit.startMonth;
    }

    private boolean isEndDate() {
        return dateLimit != null && centerYear == dateLimit.endYear && centerMonth == dateLimit.endMonth;
    }

    @NonNull
    private String getPostMonthDate(DateIndicator indicator) {
        int year = centerYear, month = centerMonth;
        if (++month > 12) {
            month = 1;
            year++;
        }
        return indicator.getDateStr(year, month);
    }

    @NonNull
    private String getPreMonthDate(DateIndicator indicator) {
        int year = centerYear, month = centerMonth;
        if (--month < 1) {
            month = 12;
            year--;
        }
        return indicator.getDateStr(year, month);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, (int) (measureWidth * 6F / 7F));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;

        criticalWidth = (int) (1F / 5F * width);
        criticalHeight = (int) (1F / 5F * height);

        int cellW = (int) (w / 7F);
        int cellH4 = (int) (h / 4F);
        int cellH5 = (int) (h / 5F);
        int cellH6 = (int) (h / 6F);

        circleRadius = cellW;

        monthSelect.onSizeChange(cellW);

        sizeDecor = (int) (cellW / 3F);
        sizeDecor2x = sizeDecor * 2;
        sizeDecor3x = sizeDecor * 3;

        sizeTextGregorian = width / 20F;
        mPaint.setTextSize(sizeTextGregorian);

        float heightGregorian = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
        sizeTextFestival = width / 40F;
        mPaint.setTextSize(sizeTextFestival);

        float heightFestival = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;
        offsetYFestival1 = (((Math.abs(mPaint.ascent() + mPaint.descent())) / 2F) +
                heightFestival / 2F + heightGregorian / 2F) / 2F;
        offsetYFestival2 = offsetYFestival1 * 2F;

        for (int i = 0; i < MONTH_REGIONS_4.length; i++) {
            for (int j = 0; j < MONTH_REGIONS_4[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH4), cellW + (j * cellW),
                        cellW + (i * cellH4));
                MONTH_REGIONS_4[i][j] = region;
            }
        }
        for (int i = 0; i < MONTH_REGIONS_5.length; i++) {
            for (int j = 0; j < MONTH_REGIONS_5[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH5), cellW + (j * cellW),
                        cellW + (i * cellH5));
                MONTH_REGIONS_5[i][j] = region;
            }
        }
        for (int i = 0; i < MONTH_REGIONS_6.length; i++) {
            for (int j = 0; j < MONTH_REGIONS_6[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH6), cellW + (j * cellW),
                        cellW + (i * cellH6));
                MONTH_REGIONS_6[i][j] = region;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mTManager.colorBG());
        if (isAllowVerScroll) {
            draw(canvas, width * indexMonth, (indexYear - 1) * height, topYear, topMonth);
            draw(canvas, width * indexMonth, (indexYear + 1) * height, bottomYear, bottomMonth);
        }

        draw(canvas, width * (indexMonth - 1), height * indexYear, leftYear, leftMonth);
        draw(canvas, width * indexMonth, indexYear * height, centerYear, centerMonth);
        draw(canvas, width * (indexMonth + 1), height * indexYear, rightYear, rightMonth);

        monthSelect.draw(canvas);

        int save = canvas.save();
        canvas.translate(width*indexMonth,indexYear*height);
        glow.onDraw(canvas);
        canvas.restoreToCount(save);
//        canvas.drawRect(0,0,1080,227,mPaint);
    }

    private void draw(Canvas canvas, int x, int y, int year, int month) {
        canvas.save();
        canvas.translate(x, y);
        DPInfo[][] info = mCManager.obtainDPInfo(year, month);
        DPInfo[][] result;
        Region[][] tmp;
        if (TextUtils.isEmpty(info[4][0].strG)) {
            tmp = MONTH_REGIONS_4;
            arrayClear(INFO_4);
            result = arrayCopy(info, INFO_4);
        } else if (TextUtils.isEmpty(info[5][0].strG)) {
            tmp = MONTH_REGIONS_5;
            arrayClear(INFO_5);
            result = arrayCopy(info, INFO_5);
        } else {
            tmp = MONTH_REGIONS_6;
            arrayClear(INFO_6);
            result = arrayCopy(info, INFO_6);
        }
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                draw(canvas, tmp[i][j].getBounds(), info[i][j]);
            }
        }
        canvas.restore();
    }

    private void draw(Canvas canvas, Rect rect, DPInfo info) {
        drawBG(canvas, rect, info);
        drawGregorian(canvas, rect, info);
        if (isFestivalDisplay) drawFestival(canvas, rect, info.strF, info.isFestival);
        drawDecor(canvas, rect, info);
    }

    private void drawBG(Canvas canvas, Rect rect, DPInfo info) {
        if (null != mDPDecor && info.isDecorBG) {
            mDPDecor.drawDecorBG(canvas, rect, mPaint,
                    centerYear + "-" + centerMonth + "-" + info.strG);
        }
        if (info.isToday && isTodayDisplay) {
            drawBGToday(canvas, rect);
        } else {
            if (isHolidayDisplay) drawBGHoliday(canvas, rect, info.isHoliday);
            if (isDeferredDisplay) drawBGDeferred(canvas, rect, info.isDeferred);
        }
    }

    private void drawBGToday(Canvas canvas, Rect rect) {
        mPaint.setColor(mTManager.colorToday());
        canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F, mPaint);
    }

    private void drawBGHoliday(Canvas canvas, Rect rect, boolean isHoliday) {
        mPaint.setColor(mTManager.colorHoliday());
        if (isHoliday) canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F, mPaint);
    }

    private void drawBGDeferred(Canvas canvas, Rect rect, boolean isDeferred) {
        mPaint.setColor(mTManager.colorDeferred());
        if (isDeferred)
            canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius / 2F, mPaint);
    }

    private void drawGregorian(Canvas canvas, Rect rect, DPInfo dpInfo) {
        mPaint.setTextSize(sizeTextGregorian);
        if (dpInfo.isWeekend) {
            mPaint.setColor(mTManager.colorWeekend());
        } else {
            mPaint.setColor(mTManager.colorG());
        }
        float y = rect.centerY();
        if (!isFestivalDisplay)
            y = rect.centerY() + Math.abs(mPaint.ascent()) - (mPaint.descent() - mPaint.ascent()) / 2F;
        if (null != mDPDecor && dpInfo.isReplaceText) {
            String data = centerYear + "-" + centerMonth + "-" + dpInfo.strG;
            mDPDecor.drawReplaceText(canvas, rect.centerX(), y, mPaint, data);
        } else {
            canvas.drawText(dpInfo.strG, rect.centerX(), y, mPaint);
        }
    }

    private void drawFestival(Canvas canvas, Rect rect, String str, boolean isFestival) {
        mPaint.setTextSize(sizeTextFestival);
        if (isFestival) {
            mPaint.setColor(mTManager.colorF());
        } else {
            mPaint.setColor(mTManager.colorL());
        }
        if (str.contains("&")) {
            String[] s = str.split("&");
            String str1 = s[0];
            if (mPaint.measureText(str1) > rect.width()) {
                float ch = mPaint.measureText(str1, 0, 1);
                int length = (int) (rect.width() / ch);
                canvas.drawText(str1.substring(0, length), rect.centerX(),
                        rect.centerY() + offsetYFestival1, mPaint);
                canvas.drawText(str1.substring(length), rect.centerX(),
                        rect.centerY() + offsetYFestival2, mPaint);
            } else {
                canvas.drawText(str1, rect.centerX(),
                        rect.centerY() + offsetYFestival1, mPaint);
                String str2 = s[1];
                if (mPaint.measureText(str2) < rect.width()) {
                    canvas.drawText(str2, rect.centerX(),
                            rect.centerY() + offsetYFestival2, mPaint);
                }
            }
        } else {
            if (mPaint.measureText(str) > rect.width()) {
                float ch = 0.0F;
                for (char c : str.toCharArray()) {
                    float tmp = mPaint.measureText(String.valueOf(c));
                    if (tmp > ch) {
                        ch = tmp;
                    }
                }
                int length = (int) (rect.width() / ch);
                canvas.drawText(str.substring(0, length), rect.centerX(),
                        rect.centerY() + offsetYFestival1, mPaint);
                canvas.drawText(str.substring(length), rect.centerX(),
                        rect.centerY() + offsetYFestival2, mPaint);
            } else {
                canvas.drawText(str, rect.centerX(),
                        rect.centerY() + offsetYFestival1, mPaint);
            }
        }
    }

    private void drawDecor(Canvas canvas, Rect rect, DPInfo info) {
        if (!TextUtils.isEmpty(info.strG)) {
            String data = centerYear + "-" + centerMonth + "-" + info.strG;
            if (null != mDPDecor && info.isDecorTL) {
                canvas.save();
                canvas.clipRect(rect.left, rect.top, rect.left + sizeDecor, rect.top + sizeDecor);
                mDPDecor.drawDecorTL(canvas, canvas.getClipBounds(), mPaint, data);
                canvas.restore();
            }
            if (null != mDPDecor && info.isDecorT) {
                canvas.save();
                canvas.clipRect(rect.left + sizeDecor, rect.top, rect.left + sizeDecor2x,
                        rect.top + sizeDecor);
                mDPDecor.drawDecorT(canvas, canvas.getClipBounds(), mPaint, data);
                canvas.restore();
            }
            if (null != mDPDecor && info.isDecorTR) {
                canvas.save();
                canvas.clipRect(rect.left + sizeDecor2x, rect.top, rect.left + sizeDecor3x,
                        rect.top + sizeDecor);
                mDPDecor.drawDecorTR(canvas, canvas.getClipBounds(), mPaint, data);
                canvas.restore();
            }
            if (null != mDPDecor && info.isDecorL) {
                canvas.save();
                canvas.clipRect(rect.left, rect.top + sizeDecor, rect.left + sizeDecor,
                        rect.top + sizeDecor2x);
                mDPDecor.drawDecorL(canvas, canvas.getClipBounds(), mPaint, data);
                canvas.restore();
            }
            if (null != mDPDecor && info.isDecorR) {
                canvas.save();
                canvas.clipRect(rect.left + sizeDecor2x, rect.top + sizeDecor,
                        rect.left + sizeDecor3x, rect.top + sizeDecor2x);
                mDPDecor.drawDecorR(canvas, canvas.getClipBounds(), mPaint, data);
                canvas.restore();
            }
        }
    }

    List<String> getDateSelected() {
        return monthSelect.getDateSelected();
    }

    void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

    public void setOnDatePickedListener(DatePicker.OnDatePickedListener onDatePickedListener) {
        monthSelect.setOnDatePickedListener(onDatePickedListener);
    }

    void setDPMode(DPMode mode) {
        monthSelect.setDPMode(mode);
    }

    void setDPDecor(DPDecor decor) {
        this.mDPDecor = decor;
    }

    DPMode getDPMode() {
        return monthSelect.getDPMode();
    }

    public void setDate(int year, int month) {
        if (month < 1) {
            month = 1;
        }
        if (month > 12) {
            month = 12;
        }

        centerYear = year;
        centerMonth = month;
        indexYear = 0;
        indexMonth = 0;
        computeDate();
        requestLayout();
        invalidate();

        if (indicator != null) {
            indicator.updateView(indicator.getPreView(), getPreMonthDate(indicator));
            indicator.updateView(indicator.getCurrentView(), indicator.getDateStr(year, month));
            indicator.updateView(indicator.getPostView(), getPostMonthDate(indicator));
        }
    }

    void setFestivalDisplay(boolean isFestivalDisplay) {
        this.isFestivalDisplay = isFestivalDisplay;
    }

    void setTodayDisplay(boolean isTodayDisplay) {
        this.isTodayDisplay = isTodayDisplay;
    }

    void setHolidayDisplay(boolean isHolidayDisplay) {
        this.isHolidayDisplay = isHolidayDisplay;
    }

    void setDeferredDisplay(boolean isDeferredDisplay) {
        this.isDeferredDisplay = isDeferredDisplay;
    }

    private void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
        invalidate();
    }

    private void arrayClear(DPInfo[][] info) {
        for (DPInfo[] anInfo : info) {
            Arrays.fill(anInfo, null);
        }
    }

    private DPInfo[][] arrayCopy(DPInfo[][] src, DPInfo[][] dst) {
        for (int i = 0; i < dst.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, dst[i].length);
        }
        return dst;
    }

    private void computeDate() {
        rightYear = leftYear = centerYear;
        topYear = centerYear - 1;
        bottomYear = centerYear + 1;

        topMonth = centerMonth;
        bottomMonth = centerMonth;

        rightMonth = centerMonth + 1;
        leftMonth = centerMonth - 1;

        if (centerMonth == 12) {
            rightYear++;
            rightMonth = 1;
        }
        if (centerMonth == 1) {
            leftYear--;
            leftMonth = 12;
        }
        if (null != onDateChangeListener) {
            onDateChangeListener.onYearChange(centerYear);
            onDateChangeListener.onMonthChange(centerMonth);
        }
    }

    public DPCManager getCManager() {
        return mCManager;
    }

    public void setAllowVerScroll(boolean allowVerScroll) {
        isAllowVerScroll = allowVerScroll;
    }

    public void setDateLimit(DateLimit dateLimit) {
        this.dateLimit = dateLimit;
    }

    public void setIndicator(DateIndicator indicator) {
        this.indicator = indicator;
    }

    public DateLimit getDateLimit() {
        return dateLimit;
    }

    public int getCenterYear() {
        return centerYear;
    }

    public int getCenterMonth() {
        return centerMonth;
    }

    interface OnDateChangeListener {
        void onMonthChange(int month);

        void onYearChange(int year);
    }

    private enum SlideMode {
        VER,
        HOR
    }

}
