package com.example.capstoneproject.Entity.Stock;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "CompanyInfo",
        foreignKeys = {@ForeignKey(entity = Stock.class,
                parentColumns = "id",
                childColumns = "stockID",
                onDelete = ForeignKey.CASCADE)})
public class CompanyInfo implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "stockID")
    private int stockID;

    @ColumnInfo(name = "symbol")
    @SerializedName("symbol")
    private String symbol;

    @ColumnInfo(name = "companyName")
    @SerializedName("companyName")
    private String name;

    @ColumnInfo(name = "sector")
    @SerializedName("sector")
    private String sector;

    @ColumnInfo(name = "empCount")
    @SerializedName("fullTimeEmployees")
    private long empCount;

    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    private String phone;

    @ColumnInfo(name = "address")
    @SerializedName("address")
    private String address;

    @ColumnInfo(name = "city")
    @SerializedName("city")
    private String city;

    @ColumnInfo(name = "state")
    @SerializedName("state")
    private String state;

    @ColumnInfo(name = "zip")
    @SerializedName("zip")
    private String zip;

    @ColumnInfo(name = "ipoDate")
    @SerializedName("ipoDate")
    private String ipoDate;

    @ColumnInfo(name = "image")
    @SerializedName("image")
    private String image;

    @ColumnInfo(name = "exchange")
    @SerializedName("exchange")
    private String exchange;

    @ColumnInfo(name = "industry")
    @SerializedName("industry")
    private String industry;

    @ColumnInfo(name = "website")
    @SerializedName("website")
    private String website;

    public CompanyInfo(int stockID, String symbol, String name, String sector, long empCount,
                       String phone, String address, String city, String state, String zip,
                       String ipoDate, String image, String exchange, String industry, String website) {
        this.stockID = stockID;
        this.symbol = symbol;
        this.name = name;
        this.sector = sector;
        this.empCount = empCount;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.ipoDate = ipoDate;
        this.image = image;
        this.exchange = exchange;
        this.industry = industry;
        this.website = website;
    }

    protected CompanyInfo(Parcel in) {
        stockID = in.readInt();
        symbol = in.readString();
        name = in.readString();
        sector = in.readString();
        empCount = in.readLong();
        phone = in.readString();
        address = in.readString();
        city = in.readString();
        state = in.readString();
        zip = in.readString();
        ipoDate = in.readString();
        image = in.readString();
        exchange = in.readString();
        industry = in.readString();
        website = in.readString();
    }

    public static final Creator<CompanyInfo> CREATOR = new Creator<CompanyInfo>() {
        @Override
        public CompanyInfo createFromParcel(Parcel in) {
            return new CompanyInfo(in);
        }

        @Override
        public CompanyInfo[] newArray(int size) {
            return new CompanyInfo[size];
        }
    };

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public long getEmpCount() {
        return empCount;
    }

    public void setEmpCount(long empCount) {
        this.empCount = empCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() { return zip; }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getIpoDate() {
        return ipoDate;
    }

    public void setIpoDate(String ipoDate) {
        this.ipoDate = ipoDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExchange() { return exchange; }

    public void setExchange(String exchange) { this.exchange = exchange; }

    public String getIndustry() { return industry; }

    public void setIndustry(String industry) { this.industry = industry; }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(stockID);
        parcel.writeString(symbol);
        parcel.writeString(name);
        parcel.writeString(sector);
        parcel.writeLong(empCount);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(zip);
        parcel.writeString(ipoDate);
        parcel.writeString(image);
        parcel.writeString(exchange);
        parcel.writeString(industry);
        parcel.writeString(website);
    }
}