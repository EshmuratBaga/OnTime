package project.ontime.kz.ontime.screen.statistic;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import project.ontime.kz.ontime.screen.statistic.fragment.MonthFragment;
import project.ontime.kz.ontime.screen.statistic.fragment.TodayFragment;
import project.ontime.kz.ontime.screen.statistic.fragment.WeekFragment;


/**
 * Created by Andrey on 4/5/2017.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabs;
    private Context context;
    private List<Integer> colors;

    public TabsPagerAdapter(Context context, FragmentManager fm, List<Integer> colors) {
        super(fm);
        this.context = context;
        this.colors = colors;
        tabs = new String[]{
                "Today",
                "Week",
                "Month",
        };
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return TodayFragment.getInstance(colors);
            case 1:
                return WeekFragment.getInstance(colors);
            case 2:
                return MonthFragment.getInstance(colors);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
