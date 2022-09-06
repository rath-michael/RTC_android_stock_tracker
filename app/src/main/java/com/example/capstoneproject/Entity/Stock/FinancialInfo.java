package com.example.capstoneproject.Entity.Stock;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "FinancialInfo",
        foreignKeys = {@ForeignKey(entity = Stock.class,
                parentColumns = "id",
                childColumns = "stockID",
                onDelete = ForeignKey.CASCADE)})
public class FinancialInfo {
    @PrimaryKey
    @ColumnInfo(name = "stockID")
    private int stockID;

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    private String symbol;

    @ColumnInfo(name = "price")
    @SerializedName("price")
    private double price;

    @ColumnInfo(name = "changePercentage")
    @SerializedName("changesPercentage")
    private double percentChange;

    @ColumnInfo(name = "change")
    @SerializedName("change")
    private double change;

    @ColumnInfo(name = "dayLow")
    @SerializedName("dayLow")
    private double dayLow;

    @ColumnInfo(name = "dayHigh")
    @SerializedName("dayHigh")
    private double dayHigh;

    @ColumnInfo(name = "yearLow")
    @SerializedName("yearLow")
    private double yearLow;

    @ColumnInfo(name = "yearHigh")
    @SerializedName("yearHigh")
    private double yearHigh;

    @ColumnInfo(name = "marketCap")
    @SerializedName("marketCap")
    private long marketCap;

    @ColumnInfo(name = "volume")
    @SerializedName("volume")
    private long volume;

    @ColumnInfo(name = "open")
    @SerializedName("open")
    private double open;

    @ColumnInfo(name = "peRatio")
    @SerializedName("pe")
    private double peRatio;

    public FinancialInfo(int stockID, String symbol, double price, double percentChange,
                         double change, double dayLow, double dayHigh, double yearLow,
                         double yearHigh, long marketCap, long volume, double open, double peRatio) {
        this.stockID = stockID;
        this.symbol = symbol;
        this.price = price;
        this.percentChange = percentChange;
        this.change = change;
        this.dayLow = dayLow;
        this.dayHigh = dayHigh;
        this.yearLow = yearLow;
        this.yearHigh = yearHigh;
        this.marketCap = marketCap;
        this.volume = volume;
        this.open = open;
        this.peRatio = peRatio;
    }

    public int getStockID() {
        return stockID;
    }

    public void setStockID(int stockID) {
        this.stockID = stockID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(double percentChange) {
        this.percentChange = percentChange;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getDayLow() {
        return dayLow;
    }

    public void setDayLow(double dayLow) {
        this.dayLow = dayLow;
    }

    public double getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(double dayHigh) {
        this.dayHigh = dayHigh;
    }

    public double getYearLow() {
        return yearLow;
    }

    public void setYearLow(double yearLow) {
        this.yearLow = yearLow;
    }

    public double getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(double yearHigh) {
        this.yearHigh = yearHigh;
    }

    public long getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(long marketCap) {
        this.marketCap = marketCap;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getPeRatio() {
        return peRatio;
    }

    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }
}
