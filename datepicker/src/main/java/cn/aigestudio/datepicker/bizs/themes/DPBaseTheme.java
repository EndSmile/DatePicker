package cn.aigestudio.datepicker.bizs.themes;

/**
 * 主题的默认实现类
 * 
 * The default implement of theme
 *
 * @author AigeStudio 2015-06-17
 */
public class DPBaseTheme extends DPTheme {
    @Override
    public Integer colorBG() {
        return null;
    }

    @Override
    public int colorBGCircle() {
        return 0x44000000;
    }

    @Override
    public int colorTitleBG() {
        return 0xFFF37B7A;
    }

    @Override
    public int colorTitle() {
        return 0xFF000000;
    }

    @Override
    public int colorBgToday() {
        return 0x88F37B7A;
    }

    @Override
    public int colorG() {
        return 0xFF333333;
    }

    @Override
    public int colorF() {
        return 0xEEC08AA4;
    }

    @Override
    public int colorWeekend() {
        return 0xFF333333;
    }

    @Override
    public int colorHoliday() {
        return 0x80FED6D6;
    }

    @Override
    public int colorDisEnableDay(){
        return 0xff999999;
    }

    @Override
    public int colorTextToday() {
        return 0xFF02B1FF;
    }

}
