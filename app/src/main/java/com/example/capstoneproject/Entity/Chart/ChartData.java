package com.example.capstoneproject.Entity.Chart;

import java.util.List;

public class ChartData {
    private List<ChartDataShort> oneDayData;
    private List<ChartDataShort> oneWeekData;
    private List<ChartDataShort> oneMonthData;
    private List<ChartDataShort> threeMonthData;
    private ChartDataLong sixMonthData;
    private ChartDataLong oneYearData;

    public ChartData(){

    }

    public List<ChartDataShort> getOneDayData() {
        return oneDayData;
    }

    public void setOneDayData(List<ChartDataShort> oneDayData) {
        this.oneDayData = oneDayData;
    }

    public List<ChartDataShort> getOneWeekData() {
        return oneWeekData;
    }

    public void setOneWeekData(List<ChartDataShort> oneWeekData) {
        this.oneWeekData = oneWeekData;
    }

    public List<ChartDataShort> getOneMonthData() {
        return oneMonthData;
    }

    public void setOneMonthData(List<ChartDataShort> oneMonthData) {
        this.oneMonthData = oneMonthData;
    }

    public List<ChartDataShort> getThreeMonthData() {
        return threeMonthData;
    }

    public void setThreeMonthData(List<ChartDataShort> threeMonthData) {
        this.threeMonthData = threeMonthData;
    }

    public ChartDataLong getSixMonthData() {
        return sixMonthData;
    }

    public void setSixMonthData(ChartDataLong sixMonthData) {
        this.sixMonthData = sixMonthData;
    }

    public ChartDataLong getOneYearData() { return  oneYearData; }

    public void setOneYearData(ChartDataLong oneYearData) { this.oneYearData = oneYearData; }
}
