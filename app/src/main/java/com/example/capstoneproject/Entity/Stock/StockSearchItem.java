package com.example.capstoneproject.Entity.Stock;

import com.google.gson.annotations.SerializedName;

public class StockSearchItem {
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("name")
    private String name;

    public StockSearchItem(String symbol, String name){
        this.symbol = symbol;
        this.name = name;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}