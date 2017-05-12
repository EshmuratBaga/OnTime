package project.ontime.kz.ontime.screen.statistic.fragment;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Andrey on 4/20/2017.
 */

public class LargeValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;
    private String mText = "";
    private int hour;
    private int minute;
    private String r;

    public LargeValueFormatter() {
        mFormat = new DecimalFormat("###,###");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return makePretty(value);
    }

    private String makePretty(double number) {

        if (number > 60){
            hour = (int) (number / 60);
            minute = (int) (number % 60);
            r = String.valueOf(hour) + "ч" + String.valueOf(minute) + "мин";
        }else {
            minute = (int) number;
            r = String.valueOf(minute) + "мин";
        }
        return r;
    }
}
