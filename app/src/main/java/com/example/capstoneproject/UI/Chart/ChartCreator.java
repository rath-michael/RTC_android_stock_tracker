package com.example.capstoneproject.UI.Chart;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.capstoneproject.Entity.Chart.ChartDataLong;
import com.example.capstoneproject.Entity.Chart.ChartDataShort;
import com.example.capstoneproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// Class to receive and hold data used in charts. Also used to format chart data into line data,
// prepare the line chart with received chart data, and return ready to use chart
public class ChartCreator {
    private static final String TAG = "CHART_CREATOR";
    private ArrayList oneDayEntries;
    private ArrayList oneWeekEntries;
    private ArrayList oneMonthEntries;
    private ArrayList threeMonthEntries;
    private ArrayList sixMonthEntries;
    private ArrayList oneYearEntries;

    private Context context;
    private LineData lineData;
    private LineDataSet lineDataSet;
    private ArrayList lineEntries;

    private Calendar calendar;

    public ChartCreator(Context context){
        this.context = context;
        calendar = Calendar.getInstance();
    }

    private void setCalendarTime(){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    // Convert 1-Day data to chart line data
    public void createOneDayEntries(List<ChartDataShort> chartData){
        calendar.setTime(chartData.get(0).getDate());
        setCalendarTime();
        Date startDate = calendar.getTime();
        Collections.reverse(chartData);

        int i = 0;
        oneDayEntries = new ArrayList<>();
        for (ChartDataShort item : chartData){
            if (item.getDate().after(startDate)){
                oneDayEntries.add(new Entry(++i, (float)item.getClose()));
            }
        }

        Log.wtf(TAG, "ONE_DAY_ENTRIES Created");
    }

    // Convert 1-Week data to chart line data
    public void createOneWeekEntries(List<ChartDataShort> chartData){
        calendar.setTime(chartData.get(0).getDate());
        setCalendarTime();
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        Date startDate = calendar.getTime();
        Collections.reverse(chartData);

        int i = 0;
        oneWeekEntries = new ArrayList<>();
        for(ChartDataShort item : chartData){
            if (item.getDate().after(startDate)){
                oneWeekEntries.add(new Entry(++i, (float)item.getClose()));
            }
        }

        Log.wtf(TAG, "ONE_WEEK_ENTRIES Created");
    }

    // Convert 1-Month data to chart line data
    public void createOneMonthEntries(List<ChartDataShort> chartData){
        calendar.setTime(chartData.get(0).getDate());
        setCalendarTime();
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        Collections.reverse(chartData);

        int i = 0;
        oneMonthEntries = new ArrayList<>();
        for(ChartDataShort item : chartData){
            if (item.getDate().after(startDate)){
                oneMonthEntries.add(new Entry(++i, (float)item.getClose()));
            }
        }

        Log.wtf(TAG, "ONE_MONTH_ENTRIES Created");
    }

    // Convert 3-Month data to chart line data
    public void createThreeMonthEntries(List<ChartDataShort> chartData){
        calendar.setTime(chartData.get(0).getDate());
        setCalendarTime();
        calendar.add(Calendar.MONTH, -3);
        Date startDate = calendar.getTime();
        Collections.reverse(chartData);

        int i = 0;
        threeMonthEntries = new ArrayList<>();
        for(ChartDataShort item : chartData){
            if (item.getDate().after(startDate)){
                threeMonthEntries.add(new Entry(++i, (float)item.getClose()));
            }
        }

        Log.wtf(TAG, "THREE_MONTH_ENTRIES Created");
    }

    // Convert 6-Month data to chart line data
    public void createSixMonthEntries(ChartDataLong chartData){
        calendar.setTime(chartData.getData().get(0).getDate());
        setCalendarTime();
        calendar.add(Calendar.MONTH, -6);
        Date startDate = calendar.getTime();
        Collections.reverse(chartData.getData());

        int i = 0;
        sixMonthEntries = new ArrayList<>();
        for(ChartDataShort item : chartData.getData()){
            if (item.getDate().after(startDate)){
                sixMonthEntries.add(new Entry(++i, (float)item.getClose()));
            }
        }

        Log.wtf(TAG, "SIX_MONTH_ENTRIES Created");
    }

    // Convert 1-Year data to chart line data
    public void createOneYearEntries(ChartDataLong chartData) {
        calendar.setTime(chartData.getData().get(0).getDate());
        setCalendarTime();
        calendar.add(Calendar.YEAR, -1);
        Date startDate = calendar.getTime();
        Collections.reverse(chartData.getData());

        int i = 0;
        oneYearEntries = new ArrayList<>();
        for (ChartDataShort item : chartData.getData()) {
            if (item.getDate().after(startDate)) {
                oneYearEntries.add(new Entry(++i, (float) item.getClose()));
            }
        }

        Log.wtf(TAG, "ONE_YEAR_ENTRIES Created");
    }

    // Assign correct line data to line chart, prepare chart for display,
    // return prepared line chart
    public LineChart createChart(LineChart lineChart, int timeFrame){
        // Assign correct data to line data by timeFrame
        switch (timeFrame){
            case 1:
                lineEntries = oneDayEntries;
                break;
            case 7:
                lineEntries = oneWeekEntries;
                break;
            case 30:
                lineEntries = oneMonthEntries;
                break;
            case 90:
                lineEntries = threeMonthEntries;
                break;
            case 180:
                lineEntries = sixMonthEntries;
                break;
            case 365:
                lineEntries = oneYearEntries;
                break;
        }
        lineDataSet = new LineDataSet(lineEntries, "");
        prepareChart(lineChart);
        return lineChart;
    }

    // Format line chart before return
    private void prepareChart(LineChart lineChart){
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(3f);

        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);

        lineChart.setBackgroundColor(ContextCompat.getColor(context,
                R.color.chart_background));

        YAxis yAxis = lineChart.getAxisLeft();
        XAxis xAxis = lineChart.getXAxis();

        yAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);

        yAxis.setSpaceTop(1);
        yAxis.setSpaceBottom(1);

        lineChart.invalidate();
    }
}
