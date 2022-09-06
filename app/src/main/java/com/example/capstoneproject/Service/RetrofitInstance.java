package com.example.capstoneproject.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;

    public static FinancialAPI getFinancialAPIService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://financialmodelingprep.com/api/v3/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(FinancialAPI.class);
    }

    public static FinancialAPI getNewsAPIService(){
        retrofit = new Retrofit
                .Builder()
                .baseUrl("https://api.marketaux.com/v1/news/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FinancialAPI.class);
    }
}
