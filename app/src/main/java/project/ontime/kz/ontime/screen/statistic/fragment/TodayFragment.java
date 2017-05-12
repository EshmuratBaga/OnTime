package project.ontime.kz.ontime.screen.statistic.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmResults;
import project.ontime.kz.ontime.R;
import project.ontime.kz.ontime.model.CubeSide;
import project.ontime.kz.ontime.model.Time;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {
    private static final String COLORS_KEY = "colorsKey";

    private PieChart pieChart;
    private LineChart lineChart;
    private BarChart barChart;
    private Realm realm;
    private long spendtime = 0;
    private long starttime;
    private long endtime;
    private String today;
    private String todaystart;
    private Date date;
    private Calendar calendar;
    private DateFormat dateFormat;
    private int faceId;
    private List<PieEntry> entries = new ArrayList<>();
    private List<Entry> entriesLine = new ArrayList<>();
    private List<BarEntry> entriesBar = new ArrayList<>();
    private List<LegendEntry> entryList = new ArrayList<>();
    private List<Integer> colors = new ArrayList<>();

    public static TodayFragment getInstance(List<Integer> colors) {
        Bundle args = new Bundle();
        TodayFragment fragment = new TodayFragment();
        args.putIntegerArrayList(COLORS_KEY, (ArrayList<Integer>) colors);
        fragment.setArguments(args);

        return fragment;
    }

    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        colors = getArguments().getIntegerArrayList(COLORS_KEY);

        pieChart = (PieChart) view.findViewById(R.id.pie_chart_today);
        lineChart = (LineChart) view.findViewById(R.id.line_chart_today);
        barChart = (BarChart) view.findViewById(R.id.bar_chart_today);

        realm = Realm.getDefaultInstance();
        RealmResults<CubeSide> cubeSides = realm.where(CubeSide.class).findAll();
        RealmResults<Time> times = realm.where(Time.class).findAll();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();

        for (int i = 0; i < cubeSides.size(); i++) {
            String name = cubeSides.get(i).getName();
            faceId = cubeSides.get(i).getId();
            for (int j = 0; j < times.size(); j++) {
                if (times.get(j).getSideId() == faceId) {
                    today = dateFormat.format(calendar.getTime());
                    starttime = times.get(j).getStartTime();
                    endtime = times.get(j).getEndTime();
                    date.setTime(starttime);
                    todaystart = dateFormat.format(date);
                    if (today.equals(todaystart) && endtime != 0) {
                        spendtime = spendtime + (times.get(j).getEndTime() - times.get(j).getStartTime());
                    }
                }
            }
            int min = (int) ((spendtime / 1000) / 60);
            entries.add(new PieEntry(i, min));
            entriesBar.add(new BarEntry(i, min));
            spendtime = 0;
        }

        initPieChart();
        initLineChart(2);
        initBarChart();
    }

    private void initLineChart(int faceId) {
        long lastDay = System.currentTimeMillis() - (24 * 60 * 60 * 1000);
        long[] spendtime = new long[6];
        List<Time> times = realm.where(Time.class).equalTo("sideId", faceId).between("startTime", lastDay, System.currentTimeMillis()).findAll();
        for (int i = 0; i < times.size(); i++) {
            if (times.get(i).getEndTime() != 0) {

                Date date = new Date(times.get(i).getEndTime());
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getDefault());
                cal.setTime(date);
                int hour = cal.get(Calendar.HOUR_OF_DAY);

                for (int j = 0; j < 24; j++) {
                    if (hour >= j && hour < j + 4) {
                        spendtime[j] = times.get(i).getEndTime() - times.get(i).getStartTime();
//                        entriesLine.add(new Entry(0, ));
                    }
                }
                Log.d("dddd", "hour" + hour);
                Log.d("dddd", "endtime" + times.get(i).getEndTime());
            }
        }
        LineDataSet dataSet = new LineDataSet(entriesLine, "LineDataSet");
        dataSet.setFillAlpha(110);
        dataSet.setColors(colors);

        final String[] quarters = new String[]{"12am", "04am", "08am", "12pm", "04pm", "08pm", "12am"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        YAxis yAxis = lineChart.getAxisRight();
        yAxis.setEnabled(false);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(6);
        xAxis.setLabelCount(6);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
        lineChart.invalidate(); // refresh
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
