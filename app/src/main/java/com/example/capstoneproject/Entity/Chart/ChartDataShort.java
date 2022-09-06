package com.example.capstoneproject.Entity.Chart;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ChartDataShort {
    @SerializedName("date")
    private Date date;

    @SerializedName("close")
    private double close;

    public Date getDate(){
        return this.date;
    }

    public double getClose(){
        return this.close;
    }
}
