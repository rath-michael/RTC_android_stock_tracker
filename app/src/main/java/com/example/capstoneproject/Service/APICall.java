package com.example.capstoneproject.Service;

import android.content.Context;
import android.util.Log;

import com.example.capstoneproject.Database.StockDatabase;
import com.example.capstoneproject.Entity.Chart.ChartDataLong;
import com.example.capstoneproject.Entity.Chart.ChartDataShort;
import com.example.capstoneproject.Entity.Chart.OneDayLoader;
import com.example.capstoneproject.Entity.Chart.OneMonthLoader;
import com.example.capstoneproject.Entity.Chart.OneWeekLoader;
import com.example.capstoneproject.Entity.Chart.OneYearLoader;
import com.example.capstoneproject.Entity.Chart.SixMonthLoader;
import com.example.capstoneproject.Entity.Chart.ThreeMonthLoader;
import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.News.News;
import com.example.capstoneproject.Entity.Stock.StockSearchItem;
import com.example.capstoneproject.UI.News.NewsLoader;
import com.example.capstoneproject.UI.Search.SymbolSearchLoader;
import com.example.capstoneproject.UI.Stock_Details.CompanyInfoLoader;
import com.example.capstoneproject.UI.Stock_Details.FinancialInfoLoader;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Class holds all the API calls and returns used in the application.
public class APICall {
    private static final String TAG = "API_CALL";
    private String apiKey = "957bad261eda9b1fcf62f8905a80c582";
    private StockDatabase stockDB;

    public APICall(Context context){
        stockDB = StockDatabase.getInstance(context);
    }

    // Call from ListFragment
    public void GetFinancialInfo(String symbol){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<FinancialInfo>> call = apiService.getFinancialInfo(symbol, apiKey);

        call.enqueue(new Callback<List<FinancialInfo>>() {
            // Catch API call response
            @Override
            public void onResponse(Call<List<FinancialInfo>> call, Response<List<FinancialInfo>> response) {
                FinancialInfo fInfo = response.body().get(0);
                // If response code is good and response isn't empty, send received data to database
                if (fInfo != null && response.code() == 200){
                    // Try to write new data to database
                    try {
                        stockDB.stockDAO().updateFinancialInfo(fInfo.getSymbol(), fInfo.getPrice(), fInfo.getPercentChange(), fInfo.getChange(),
                                fInfo.getDayLow(), fInfo.getDayHigh(), fInfo.getOpen());
                        Log.wtf(TAG, "FINANCIAL_INFO_WRITE_SUCCESS");
                    }
                    // Catch database write failure, log error
                    catch (Exception ex){
                        Log.wtf(TAG, "FINANCIAL_INFO_WRITE_FAILURE: " + ex.getMessage());
                    }
                }
                // Else log error
                else {
                    Log.wtf(TAG, "FINANCIAL_INFO_RESPONSE_CODE: " + response.code());
                }
            }
            // Catch API call failure
            @Override
            public void onFailure(Call<List<FinancialInfo>> call, Throwable t) {
                Log.wtf(TAG, "FINANCIAL_INFO_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from SearchFragment
    public void GetFinancialInfo(String symbol, FinancialInfoLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<FinancialInfo>> call = apiService.getFinancialInfo(symbol, apiKey);

        call.enqueue(new Callback<List<FinancialInfo>>() {
            // API call response
            @Override
            public void onResponse(Call<List<FinancialInfo>> call, Response<List<FinancialInfo>> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.onFinancialInfoLoaded(response.body().get(0));
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.onFinancialInfoLoaded(null);
                }
                Log.wtf(TAG, "FINANCIAL_INFO_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<FinancialInfo>> call, Throwable t) {
                loader.onFinancialInfoLoaded(null);
                Log.wtf(TAG, "FINANCIAL_INFO_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment
    public void GetCompanyInfo(String symbol, CompanyInfoLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<CompanyInfo>> call = apiService.getCompanyInfo(symbol, apiKey);

        call.enqueue(new Callback<List<CompanyInfo>>() {
            // API call response
            @Override
            public void onResponse(Call<List<CompanyInfo>> call, Response<List<CompanyInfo>> response) {
                // If response code is good and response isn't empty, send data to to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.onCompanyInfoLoaded(response.body().get(0));
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.onCompanyInfoLoaded(null);
                }
                Log.wtf(TAG, "COMPANY_INFO_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<CompanyInfo>> call, Throwable t) {
                loader.onCompanyInfoLoaded(null);
                Log.wtf(TAG, "COMPANY_INFO_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment & NewsFragment
    public void GetNews(String symbol, NewsLoader loader){
        apiKey = "l7wp9xFNalmAm9Kjmsa7LHgRlKbdrLtB1XC8zdJO";
        FinancialAPI apiService = RetrofitInstance.getNewsAPIService();
        Call<News> call = apiService.getNews(symbol, "en", "3", apiKey);

        call.enqueue(new Callback<News>() {
            // API call response
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                // If response isn't empty and code is good, send data to DetailsFragment/NewsFragment receiver
                if (response != null && response.code() == 200){
                    loader.onNewsLoaded(response.body());
                }
                // Else send empty response to DetailsFragment/NewsFragment
                else {
                    loader.onNewsLoaded(null);
                }
                Log.wtf(TAG, "NEWS_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                loader.onNewsLoaded(null);
                Log.wtf(TAG, "NEWS_FAILURE: " + t.getMessage());
            }
        });
    }


    // Call from SearchFragment
    public void SearchForTicker(String symbol, SymbolSearchLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<StockSearchItem>> call = apiService.searchForStock(symbol, "10", "NASDAQ,NYSE,ETF,MUTUAL_FUND,CRYPTO", apiKey);

        call.enqueue(new Callback<List<StockSearchItem>>() {
            // API call response
            @Override
            public void onResponse(Call<List<StockSearchItem>> call, Response<List<StockSearchItem>> response) {
                // If response isn't empty and code is good, send data to SearchFragment receiver
                if (response != null && response.code() == 200){
                    loader.onSymbolSearchLoaded(response.body());
                }
                // Else send empty response to SearchFragment
                else {
                    loader.onSymbolSearchLoaded(null);
                }
                Log.wtf(TAG, "SYMBOL_SEARCH_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<StockSearchItem>> call, Throwable t) {
                loader.onSymbolSearchLoaded(null);
                Log.wtf(TAG, "SYMBOL_SEARCH_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment for 1-Day chart
    public void getOneDayData(String symbol, OneDayLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<ChartDataShort>> call = apiService.get1MinuteData(symbol, apiKey);

        call.enqueue(new Callback<List<ChartDataShort>>() {
            // API call response
            @Override
            public void onResponse(Call<List<ChartDataShort>> call, Response<List<ChartDataShort>> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.oneDayDataLoaded(response.body());
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.oneDayDataLoaded(null);
                }
                Log.wtf(TAG, "ONE_DAY_DATA_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<ChartDataShort>> call, Throwable t) {
                loader.oneDayDataLoaded(null);
                Log.wtf(TAG, "ONE_DAY_DATA_FAILURE:  " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment for 1-Week chart
    public void getOneWeekData(String symbol, OneWeekLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<ChartDataShort>> call = apiService.get5MinuteData(symbol, apiKey);

        call.enqueue(new Callback<List<ChartDataShort>>() {

            // API call response
            @Override
            public void onResponse(Call<List<ChartDataShort>> call, Response<List<ChartDataShort>> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.oneWeekDataLoaded(response.body());
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.oneWeekDataLoaded(null);
                }
                Log.wtf(TAG, "ONE_WEEK_DATA_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<ChartDataShort>> call, Throwable t) {
                loader.oneWeekDataLoaded(null);
                Log.wtf(TAG, "ONE_WEEK_DATA_FAILURE:  " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment for 1-Month chart
    public void getOneMonthData(String symbol, OneMonthLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<ChartDataShort>> call = apiService.get30MinuteData(symbol, apiKey);

        call.enqueue(new Callback<List<ChartDataShort>>() {
            // API call response
            @Override
            public void onResponse(Call<List<ChartDataShort>> call, Response<List<ChartDataShort>> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.oneMonthDataLoaded(response.body());
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.oneMonthDataLoaded(null);
                }
                Log.wtf(TAG, "ONE_MONTH_DATA_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<ChartDataShort>> call, Throwable t) {
                loader.oneMonthDataLoaded(null);
                Log.wtf(TAG, "ONE_MONTH_DATA_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment for 3-Month chart
    public void getThreeMonthData(String symbol, ThreeMonthLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<List<ChartDataShort>> call = apiService.get4HourData(symbol, apiKey);

        call.enqueue(new Callback<List<ChartDataShort>>() {
            // API call response
            @Override
            public void onResponse(Call<List<ChartDataShort>> call, Response<List<ChartDataShort>> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.threeMonthDataLoaded(response.body());
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.threeMonthDataLoaded(null);
                }
                Log.wtf(TAG, "THREE_MONTH_DATA_SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<List<ChartDataShort>> call, Throwable t) {
                loader.threeMonthDataLoaded(null);
                Log.wtf(TAG, "THREE_MONTH_DATA_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment for 6-Month chart
    public void getSixMonthData(String symbol, SixMonthLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<ChartDataLong> call = apiService.get1DayData(symbol, "line", apiKey);

        call.enqueue(new Callback<ChartDataLong>() {
            // API call response
            @Override
            public void onResponse(Call<ChartDataLong> call, Response<ChartDataLong> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.sixMonthDataLoaded(response.body());
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.sixMonthDataLoaded(null);
                }

                Log.wtf(TAG, "SIX_MONTH_DATA SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<ChartDataLong> call, Throwable t) {
                loader.sixMonthDataLoaded(null);
                Log.wtf(TAG, "SIX_MONTH_DATA_FAILURE: " + t.getMessage());
            }
        });
    }

    // Call from StockDetailsFragment for 1-Year chart
    public void getOneYearData(String symbol, OneYearLoader loader){
        FinancialAPI apiService = RetrofitInstance.getFinancialAPIService();
        Call<ChartDataLong> call = apiService.get1DayData(symbol, "line", apiKey);

        call.enqueue(new Callback<ChartDataLong>() {
            // API call response
            @Override
            public void onResponse(Call<ChartDataLong> call, Response<ChartDataLong> response) {
                // If response isn't empty and code is good, send data to DetailsFragment receiver
                if (response != null && response.code() == 200){
                    loader.oneYearDataLoaded(response.body());
                }
                // Else send empty response to DetailsFragment
                else {
                    loader.oneYearDataLoaded(null);
                }

                Log.wtf(TAG, "ONE_YEAR_DATA SUCCESS | CODE: " + response.code());
            }

            // API call failure
            @Override
            public void onFailure(Call<ChartDataLong> call, Throwable t) {
                loader.oneYearDataLoaded(null);
                Log.wtf(TAG, "ONE_YEAR_DATA_FAILURE: " + t.getMessage());
            }
        });
    }
}
