package com.example.capstoneproject.Entity.Stock;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "StockMaster")
public class Stock implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "symbol")
    private String symbol;

    @Ignore
    private FinancialInfo financialInfo;

    @Ignore
    private CompanyInfo companyInfo;

    public Stock(String symbol) {
        this.symbol = symbol;
    }

    protected Stock(Parcel in) {
        id = in.readInt();
        symbol = in.readString();
        companyInfo = in.readParcelable(CompanyInfo.class.getClassLoader());
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel in) {
            return new Stock(in);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public FinancialInfo getFinancialInfo() { return financialInfo; }

    public void setFinancialInfo(FinancialInfo financialInfo) { this.financialInfo = financialInfo; }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(symbol);
        parcel.writeParcelable(companyInfo, i);
    }
}
