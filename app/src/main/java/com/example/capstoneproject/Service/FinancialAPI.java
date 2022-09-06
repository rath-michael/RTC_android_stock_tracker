package com.example.capstoneproject.Service;

import com.example.capstoneproject.Entity.Chart.ChartDataLong;
import com.example.capstoneproject.Entity.Chart.ChartDataShort;
import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.News.News;
import com.example.capstoneproject.Entity.Stock.StockSearchItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FinancialAPI {
    @GET("quote/{symbol}/")
    Call<List<FinancialInfo>> getFinancialInfo(@Path("symbol") String symbol, @Query("apikey") String apiKey);

    @GET("profile/{symbol}/")
    Call<List<CompanyInfo>> getCompanyInfo(@Path("symbol") String symbol, @Query("apikey") String apiKey);

    @GET("all")
    Call<News> getNews(@Query("symbols") String symbol, @Query("language") String language, @Query("limit") String limit,
                       @Query("api_token") String apikey);

    @GET("search/")
    Call<List<StockSearchItem>> searchForStock(@Query("query") String symbol, @Query("limit") String limit,
                                               @Query("exchange") String exchange, @Query("apikey") String apiKey);

    @GET("historical-chart/1min/{symbol}")
    Call<List<ChartDataShort>> get1MinuteData(@Path("symbol") String symbol, @Query("apikey") String apikey);

    @GET("historical-chart/5min/{symbol}")
    Call<List<ChartDataShort>> get5MinuteData(@Path("symbol") String symbol, @Query("apikey") String apikey);

    @GET("historical-chart/30min/{symbol}")
    Call<List<ChartDataShort>> get30MinuteData(@Path("symbol") String symbol, @Query("apikey") String apikey);

    @GET("historical-chart/4hour/{symbol}")
    Call<List<ChartDataShort>> get4HourData(@Path("symbol") String symbol, @Query("apikey") String apikey);

    @GET("historical-price-full/{symbol}/")
    Call<ChartDataLong> get1DayData(@Path("symbol") String symbol, @Query("seriestype") String seriesType,
                                    @Query("apikey") String apikey);
}
