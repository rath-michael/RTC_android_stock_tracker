package com.example.capstoneproject.Entity.Chart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChartDataLong {
    @SerializedName("symbol")
    private String symbol;

    @SerializedName("historical")
    private List<ChartDataShort> data;

    public List<ChartDataShort> getData() {
        return data;
    }

    public void setData(List<ChartDataShort> data) {
        this.data = data;
    }
}
