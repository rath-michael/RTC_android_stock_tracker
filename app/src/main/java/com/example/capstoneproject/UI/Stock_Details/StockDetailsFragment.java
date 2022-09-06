package com.example.capstoneproject.UI.Stock_Details;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstoneproject.Database.StockDatabase;
import com.example.capstoneproject.Entity.Chart.ChartData;
import com.example.capstoneproject.Entity.Chart.ChartDataLong;
import com.example.capstoneproject.Entity.Chart.ChartDataShort;
import com.example.capstoneproject.Entity.Chart.OneDayLoader;
import com.example.capstoneproject.Entity.Chart.OneMonthLoader;
import com.example.capstoneproject.Entity.Chart.OneWeekLoader;
import com.example.capstoneproject.Entity.Chart.OneYearLoader;
import com.example.capstoneproject.Entity.Chart.SixMonthLoader;
import com.example.capstoneproject.Entity.Chart.ThreeMonthLoader;
import com.example.capstoneproject.Entity.News.News;
import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.Stock.Stock;
import com.example.capstoneproject.R;
import com.example.capstoneproject.Service.APICall;
import com.example.capstoneproject.UI.Chart.ChartCreator;
import com.example.capstoneproject.UI.News.NewsAdapter;
import com.example.capstoneproject.UI.News.NewsLoader;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

// Fragment to display financial and company details about a specific stock. This fragment can be
// reached from either the ListFragment, or from the SearchFragment. If coming from the ListFragment,
// the necessary data will be pulled from the database and displayed. If coming from the SearchFragment,
// API calls will be made to get the data. At the same time, an API call is made to retrieve that data
// for the chart for a 1-day timeline. Each time the user selects a different time frame (for example,
// 1 week, 1 month, 1 year), a new API call is made to get the required data, the data is formatted,
// a chart is created and display. This chart functionality is the same if coming from either the
// ListFragment or SearchFragment.
public class StockDetailsFragment extends Fragment implements NewsLoader, OneDayLoader, OneWeekLoader,
        OneMonthLoader, ThreeMonthLoader, SixMonthLoader, OneYearLoader {

    private static final String TAG = "STOCK_DETAILS_FRAGMENT";
    private StockDatabase stockDB;

    private Stock inputStock;
    private CompanyInfo companyInfo;
    private FinancialInfo financialInfo;

    private ConstraintLayout progressBar;
    private ProgressBar progressBarChart;
    private TextView txtSymbol, txtName, txtPrice, txtPerChance, txtChange, txtOpen, txtHigh,
            txtLow, txtPE, txtVolume, txtMktCap, txtChartError;
    private Button btnSave, btnDelete, btnDay, btnWeek, btnMonth, btnThreeMonth, btnSixMonth, btnYear;
    private ImageButton btnCompanyInfo;

    private ChartData chartData;
    private LineChart lineChart;
    private RecyclerView rclView;
    private ChartCreator chartCreator;

    private boolean newsLoaded = false;

    public StockDetailsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get financial/company info if available
        if (getArguments() != null) {
            inputStock = getArguments().getParcelable("Stock");
            financialInfo = inputStock.getFinancialInfo();
            companyInfo = inputStock.getCompanyInfo();
        }

        // Get database and chart data instance
        stockDB = StockDatabase.getInstance(getContext());
        chartData = new ChartData();

        // API calls to get news and 1-day chart data for stock
        APICall call = new APICall(getContext());
        call.getOneDayData(inputStock.getSymbol(), this::oneDayDataLoaded);
        call.GetNews(inputStock.getSymbol(), this::onNewsLoaded);

        // New ChartCreator
        chartCreator = new ChartCreator(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_stock_details, container, false);

        // Create/assign all UI elements
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        lineChart = root.findViewById(R.id.lineChart);

        progressBarChart = root.findViewById(R.id.progressBarChart);

        txtChartError = root.findViewById(R.id.txtChartError);

        txtSymbol = root.findViewById(R.id.txtSymbol);
        txtName = root.findViewById(R.id.txtName);
        txtPrice = root.findViewById(R.id.txtPrice);
        txtPerChance = root.findViewById(R.id.txtPerChange);
        txtChange = root.findViewById(R.id.txtChange);
        txtOpen = root.findViewById(R.id.txtOpen);
        txtHigh = root.findViewById(R.id.txtHigh);
        txtLow = root.findViewById(R.id.txtLow);
        txtVolume = root.findViewById(R.id.txtVolume);
        txtPE = root.findViewById(R.id.txtPE);
        txtMktCap = root.findViewById(R.id.txtMarketCap);
        btnSave = root.findViewById(R.id.btnSave);
        btnDelete = root.findViewById(R.id.btnDelete);

        btnDay = root.findViewById(R.id.btnDay);
        btnDay.setOnClickListener(timeListener);
        btnWeek = root.findViewById(R.id.btnWeek);
        btnWeek.setOnClickListener(timeListener);
        btnMonth = root.findViewById(R.id.btnMonth);
        btnMonth.setOnClickListener(timeListener);
        btnThreeMonth = root.findViewById(R.id.btnThreeMonth);
        btnThreeMonth.setOnClickListener(timeListener);
        btnSixMonth = root.findViewById(R.id.btnSixMonth);
        btnSixMonth.setOnClickListener(timeListener);
        btnYear = root.findViewById(R.id.btnYear);
        btnYear.setOnClickListener(timeListener);
        btnCompanyInfo = root.findViewById(R.id.btnCompanyInfo);

        displayFinancialInfo();

        // Check if stock already exists in database. If yes, remove button
        // made visible. If no, save button made visible
        boolean exists = stockDB.stockDAO().stockExists(inputStock.getSymbol());
        if (exists){
            btnSave.setVisibility(View.INVISIBLE);
            btnSave.setEnabled(false);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setEnabled(true);
        }
        else {
            btnSave.setVisibility(View.VISIBLE);
            btnSave.setEnabled(true);
            btnDelete.setVisibility(View.INVISIBLE);
            btnDelete.setEnabled(false);
        }

        rclView = root.findViewById(R.id.rclViewNews);

        // Check loading progress every (1) second until
        // ready to remove progress loading animation
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (newsLoaded){
                    removeProgressBar();
                    return;
                }
                handler.postDelayed(this, 1000);
            }
        });

        return root;
    }


    // Method to remove progress bar
    private void removeProgressBar(){
        progressBar.setVisibility(View.GONE);
    }

    // All financial information displayed in UI
    private void displayFinancialInfo(){
        txtSymbol.setText(companyInfo.getSymbol());
        txtName.setText(companyInfo.getName());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        txtPrice.setText(currencyFormat.format(financialInfo.getPrice()));
        txtOpen.append(currencyFormat.format(financialInfo.getOpen()));
        txtHigh.append(currencyFormat.format(financialInfo.getDayHigh()));
        txtLow.append(currencyFormat.format(financialInfo.getDayLow()));

        String percChange = String.format("%.2f", financialInfo.getPercentChange());
        txtPerChance.setText(percChange + "%");

        if (financialInfo.getPercentChange() >= 0){
            txtPerChance.setTextColor(ContextCompat.getColor(getContext(), R.color.line_green));
        }
        else {
            txtPerChance.setTextColor(ContextCompat.getColor(getContext(), R.color.line_red));
        }

        String change = String.format("%.2f", financialInfo.getChange());
        txtChange.setText("$" + change);
        if (financialInfo.getChange() >= 0){
            txtChange.setTextColor(ContextCompat.getColor(getContext(), R.color.line_green));
        }
        else {
            txtChange.setTextColor(ContextCompat.getColor(getContext(), R.color.line_red));
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        txtVolume.append(numberFormat.format(financialInfo.getVolume()));
        txtMktCap.append(numberFormat.format(financialInfo.getMarketCap()));
        txtPE.append(numberFormat.format(financialInfo.getPeRatio()));
    }

    // Save/Delete/Dialog button listeners to be used after fragment is created
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Save stock to database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (financialInfo != null && companyInfo != null){
                        Stock newStock = new Stock(inputStock.getSymbol());
                        stockDB.stockDAO().insertNewStock(newStock);

                        int id = stockDB.stockDAO().getMostRecentId();
                        financialInfo.setStockID(id);
                        companyInfo.setStockID(id);

                        stockDB.stockDAO().insertFinancialInfo(financialInfo);
                        stockDB.stockDAO().insertCompanyInfo(companyInfo);

                        Log.wtf(TAG, "ADDED NEW STOCK TO DB");
                        Toast.makeText(getContext(), "Stock successfully added", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Log.wtf(TAG, "FAILED TO ADD STOCK TO DB " + e.getMessage());
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete stock from database
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    stockDB.stockDAO().deleteStock(inputStock.getId());
                    Toast.makeText(getContext(), "Stock successfully deleted", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.wtf(TAG, "Database_Delete_Error: " + e.getMessage());
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Display company information dialog
        btnCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.company_info_dialog);

                TextView cName = dialog.findViewById(R.id.cInfoName);
                cName.setText(companyInfo.getName());

                TextView cSector = dialog.findViewById(R.id.cInfoSector);
                cSector.setText("Sector: " + companyInfo.getSector());

                TextView cEmpCount = dialog.findViewById(R.id.cInfoEmpCount);
                String employeeCount = String.format("%,d", companyInfo.getEmpCount());
                cEmpCount.setText(employeeCount + " Employees");

                TextView cPhone = dialog.findViewById(R.id.cInfoPhone);
                String phoneNumber = PhoneNumberUtils.formatNumber(companyInfo.getPhone());
                cPhone.setText(phoneNumber);

                TextView cAddressLineOne = dialog.findViewById(R.id.cInfoAddressOne);
                cAddressLineOne.setText(companyInfo.getAddress());

                TextView cAddressLineTwo = dialog.findViewById(R.id.cInfoAddressTwo);
                cAddressLineTwo.setText(companyInfo.getCity() + ", " + companyInfo.getState() + " " + companyInfo.getZip());

                TextView cIPODate = dialog.findViewById(R.id.cInfoIPO);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = format.parse(companyInfo.getIpoDate());
                    cIPODate.setText("IPO Date: " + date);
                } catch (ParseException e) {
                    Log.wtf(TAG, "INFO_DATE_PARSE_ERROR: " + e.getMessage());
                }

                TextView cExchange = dialog.findViewById(R.id.cInfoExchange);
                cExchange.setText("Exchange: " + companyInfo.getExchange());

                TextView cIndustry = dialog.findViewById(R.id.cInfoIndustry);
                cIndustry.setText("Industry: " + companyInfo.getIndustry());

                // Text click takes user to company website
                TextView cWebsite = dialog.findViewById(R.id.cInfoWebsite);
                cWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(companyInfo.getWebsite()));
                        startActivity(browserIntent);
                    }
                });

                ImageView cImage = dialog.findViewById(R.id.cInfoImage);
                Picasso.get().load(companyInfo.getImage()).into(cImage);

                dialog.show();
            }
        });
    }

    // Button listener to select chart time frame
    private View.OnClickListener timeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            APICall call =  new APICall(getContext());

            lineChart.setVisibility(View.INVISIBLE);
            txtChartError.setVisibility(View.GONE);
            progressBarChart.setVisibility(View.VISIBLE);

            // Switch based off which time frame button is selected
            switch (view.getId()){
                case R.id.btnDay:
                    // If 1-Day chart data already exists, send existing data to
                    // method used to receive data and create chart
                    if (chartData.getOneDayData() == null){
                        call.getOneDayData(inputStock.getSymbol(), StockDetailsFragment.this::oneDayDataLoaded);
                    }
                    // Else new API call to get 1-Day chart data
                    else {
                        lineChart = chartCreator.createChart(lineChart, 1);
                        lineChart.invalidate();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBarChart.setVisibility(View.GONE);
                    }
                    break;

                case R.id.btnWeek:
                    // If 1-Week chart data already exists, send existing data to
                    // method used to receive data and create chart
                    if (chartData.getOneWeekData() == null){
                        call.getOneWeekData(inputStock.getSymbol(), StockDetailsFragment.this::oneWeekDataLoaded);
                    }
                    // Else new API call to get 1-Week chart data
                    else {
                        lineChart = chartCreator.createChart(lineChart, 7);
                        lineChart.invalidate();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBarChart.setVisibility(View.GONE);
                    }
                    break;

                case R.id.btnMonth:
                    // If 1-Month chart data already exists, send existing data to
                    // method used to receive data and create chart
                    if (chartData.getOneMonthData() == null){
                        call.getOneMonthData(inputStock.getSymbol(), StockDetailsFragment.this::oneMonthDataLoaded);
                    }
                    // Else new API call to get 1-Month chart data
                    else {
                        lineChart = chartCreator.createChart(lineChart, 30);
                        lineChart.invalidate();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBarChart.setVisibility(View.GONE);
                    }
                    break;

                case R.id.btnThreeMonth:
                    // If 3-Month chart data already exists, send existing data to
                    // method used to receive data and create chart
                    if (chartData.getThreeMonthData() == null){
                        call.getThreeMonthData(inputStock.getSymbol(), StockDetailsFragment.this::threeMonthDataLoaded);
                    }
                    // Else new API call to get 3-Month chart data
                    else {
                        lineChart = chartCreator.createChart(lineChart, 90);
                        lineChart.invalidate();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBarChart.setVisibility(View.GONE);
                    }
                    break;


                case R.id.btnSixMonth:
                    // If 6-Month chart data already exists, send existing data to
                    // method used to receive data and create chart
                    if (chartData.getSixMonthData() == null){
                        call.getSixMonthData(inputStock.getSymbol(), StockDetailsFragment.this::sixMonthDataLoaded);
                    }
                    // Else new API call to get 6-Month chart data
                    else {
                        lineChart = chartCreator.createChart(lineChart, 180);
                        lineChart.invalidate();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBarChart.setVisibility(View.GONE);
                    }
                    break;

                case R.id.btnYear:
                    // If 1-Year chart data already exists, send existing data to
                    // method used to receive data and create chart
                    if (chartData.getOneYearData() == null){
                        call.getOneYearData(inputStock.getSymbol(), StockDetailsFragment.this::oneYearDataLoaded);
                    }
                    // Else new API call to get 1-Year chart data
                    else {
                        lineChart = chartCreator.createChart(lineChart, 365);
                        lineChart.invalidate();
                        lineChart.setVisibility(View.VISIBLE);
                        progressBarChart.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };


    // Method to receive 1-Day chart data, create and display 1-Day chart
    @Override
    public void oneDayDataLoaded(List<ChartDataShort> data){
        progressBarChart.setVisibility(View.GONE);

        // If successful, display chart
        if (data != null){
            lineChart.setVisibility(View.VISIBLE);
            txtChartError.setVisibility(View.GONE);
            chartData.setOneDayData(data);
            chartCreator.createOneDayEntries(data);
            lineChart = chartCreator.createChart(lineChart, 1);
            lineChart.invalidate();
            lineChart.setVisibility(View.VISIBLE);
        }
        // Else display error message
        else {
            lineChart.setVisibility(View.GONE);
            txtChartError.setVisibility(View.VISIBLE);
        }
    }

    // Method to receive 1-Week chart data, create and display 1-Day chart
    @Override
    public void oneWeekDataLoaded(List<ChartDataShort> data){
        progressBarChart.setVisibility(View.GONE);

        // If successful, display chart
        if (data != null){
            lineChart.setVisibility(View.VISIBLE);
            txtChartError.setVisibility(View.GONE);
            chartData.setOneWeekData(data);
            chartCreator.createOneWeekEntries(data);
            lineChart = chartCreator.createChart(lineChart, 7);
            lineChart.invalidate();
            lineChart.setVisibility(View.VISIBLE);
        }
        // Else display error message
        else {
            lineChart.setVisibility(View.GONE);
            txtChartError.setVisibility(View.VISIBLE);
        }
    }

    // Method to receive 1-Month chart data, create and display 1-Day chart
    @Override
    public void oneMonthDataLoaded(List<ChartDataShort> data){
        progressBarChart.setVisibility(View.GONE);

        // If successful, display chart
        if (data != null){
            lineChart.setVisibility(View.VISIBLE);
            txtChartError.setVisibility(View.GONE);
            chartData.setOneMonthData(data);
            chartCreator.createOneMonthEntries(data);
            lineChart = chartCreator.createChart(lineChart, 30);
            lineChart.invalidate();
            lineChart.setVisibility(View.VISIBLE);
        }
        // Else display error message
        else {
            lineChart.setVisibility(View.GONE);
            txtChartError.setVisibility(View.VISIBLE);
        }
    }

    // Method to receive 3-Month chart data, create and display 1-Day chart
    @Override
    public void threeMonthDataLoaded(List<ChartDataShort> data){
        progressBarChart.setVisibility(View.GONE);

        // If successful, display chart
        if (data != null){
            lineChart.setVisibility(View.VISIBLE);
            txtChartError.setVisibility(View.GONE);
            chartData.setThreeMonthData(data);
            chartCreator.createThreeMonthEntries(data);
            lineChart = chartCreator.createChart(lineChart, 90);
            lineChart.invalidate();
            lineChart.setVisibility(View.VISIBLE);
        }
        // Else display error message
        else {
            lineChart.setVisibility(View.GONE);
            txtChartError.setVisibility(View.VISIBLE);
        }
    }

    // Method to receive 6-Month chart data, create and display 1-Day chart
    @Override
    public void sixMonthDataLoaded(ChartDataLong data){
        progressBarChart.setVisibility(View.GONE);

        // If successful, display chart
        if (data != null){
            lineChart.setVisibility(View.VISIBLE);
            txtChartError.setVisibility(View.GONE);
            chartData.setSixMonthData(data);
            chartCreator.createSixMonthEntries(data);
            lineChart = chartCreator.createChart(lineChart, 180);
            lineChart.invalidate();
            lineChart.setVisibility(View.VISIBLE);
        }
        // Else display error message
        else {
            lineChart.setVisibility(View.GONE);
            txtChartError.setVisibility(View.VISIBLE);
            progressBarChart.setVisibility(View.GONE);
        }
    }

    // Method to receive 1-Year chart data, create and display 1-Day chart
    @Override
    public void oneYearDataLoaded(ChartDataLong data){
        progressBarChart.setVisibility(View.GONE);

        // If successful, display chart
        if (data != null){
            lineChart.setVisibility(View.VISIBLE);
            txtChartError.setVisibility(View.GONE);
            chartData.setOneYearData(data);
            chartCreator.createOneYearEntries(data);
            lineChart = chartCreator.createChart(lineChart, 365);
            lineChart.invalidate();
            lineChart.setVisibility(View.VISIBLE);
        }
        // Else display error message
        else {
            lineChart.setVisibility(View.GONE);
            txtChartError.setVisibility(View.VISIBLE);
        }
    }

    // Method to receive news data, create and display news adapter
    @Override
    public void onNewsLoaded(News newsList){
        // If successful, display news
        if (newsList != null){
            NewsAdapter adapter = new NewsAdapter(newsList.getArticles());
            rclView.setLayoutManager(new LinearLayoutManager(getContext()));
            rclView.setAdapter(adapter);
        }
        newsLoaded = true;
    }
}