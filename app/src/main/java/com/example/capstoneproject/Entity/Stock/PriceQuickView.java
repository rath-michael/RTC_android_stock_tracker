package com.example.capstoneproject.Entity.Stock;

public class PriceQuickView {
    private String symbol;
    private double price;
    private double changePercentage;
    private double change;

    public PriceQuickView(String symbol, double price, double changePercentage, double change) {
        this.symbol = symbol;
        this.price = price;
        this.changePercentage = changePercentage;
        this.change = change;
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

    public double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(double percentChange) {
        this.changePercentage = percentChange;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }
}