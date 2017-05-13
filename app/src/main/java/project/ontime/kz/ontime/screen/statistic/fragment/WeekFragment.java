package project.ontime.kz.ontime.screen.statistic.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.model.Time;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeekFragment extends Fragment {
    private static final String COLORS_KEY = "colorsKeyWeek";

    private PieChart pieChart;
    private LineChart lineChart;
    private BarChart barChart;
    private Realm realm;
    private long spendtime = 0;
    private long starttime;
    private long endtime;
    private long weekMillisecond;
    private long today;
    private Calendar calendar;
    private int faceId;
    private List<PieEntry> entries = new ArrayList<>();
    private List<Entry> entriesLine = new ArrayList<>();
    private List<BarEntry> entriesBar = new ArrayList<>();
    private List<LegendEntry> entryList = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();

    public static WeekFragment getInstance(List<Integer> colors) {
        Bundle args = new Bundle();
        WeekFragment fragment = new WeekFragment();
        args.putIntegerArrayList(COLORS_KEY, (ArrayList<Integer>) colors);
        fragment.setArguments(args);

        return fragment;
    }

    public WeekFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_week, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        colors = getArguments().getIntegerArrayList(COLORS_KEY);

        pieChart = (PieChart) view.findViewById(R.id.pie_chart_week);
        lineChart = (LineChart) view.findViewById(R.id.line_chart_week);
        barChart = (BarChart) view.findViewById(R.id.bar_chart_week);

        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> cubeSides = realm.where(CubeSide.class).findAll();
        RealmResults<Time> times = realm.where(Time.class).findAll();

        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        weekMillisecond = calendar.getTimeInMillis();
        today = System.currentTimeMillis();

        for (int i = 0; i < cubeSides.size(); i++) {
            faceId = cubeSides.get(i).getId();
            for (int j = 0; j < times.size(); j++) {
                if (times.get(j).getSideId() == faceId){
                    starttime = times.get(j).getStartTime();
                    endtime = times.get(j).getEndTime();
                    if (weekMillisecond <= starttime && starttime < today && endtime != 0){
                        spendtime = spendtime + (times.get(j).getEndTime() - times.get(j).getStartTime());
                    }
                }
            }
            int min = (int) ((spendtime / 1000) / 60);
            entriesBar.add(new BarEntry(i, min));
            entries.add(new PieEntry(i, min));
            spendtime = 0;
        }

        initPieChart();
        initBarChart();
    }

    private void initPieChart() {
        PieDataSet set = new PieDataSet(entries, "PieDataSet");
        set.setColors(colors);
        set.setValueFormatter(new PercentFormatter());

        Legend l = pieChart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setTextSize(12f);
        l.setTextColor(Color.BLACK);
        l.setCustom(entryList);

        PieData data = new PieData(set);
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(35f);
        pieChart.setHoleRadius(35f);
        pieChart.animateXY(1400, 1400);
        pieChart.setData(data);
        pieChart.invalidate(); // refresh
    }

    private void initBarChart() {
        BarDataSet set = new BarDataSet(entriesBar, "BarDataSet");
        set.setColors(colors);
        set.setValueFormatter(new LargeValueFormatter());

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width
        barChart.setData(data);
        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars
        barChart.invalidate(); // refresh
    }
}
