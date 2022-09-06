package com.example.capstoneproject.UI.List;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstoneproject.Database.StockDatabase;
import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.Stock.PriceQuickView;
import com.example.capstoneproject.Entity.Stock.Stock;
import com.example.capstoneproject.R;
import com.example.capstoneproject.Service.APICall;
import com.example.capstoneproject.UI.Stock_Details.StockDetailsFragment;

import java.util.List;

// This fragment is the default page of the application. The page displays the user selected short
// list of stocks. The only data displayed is the symbol, the current price, and the current
// percent change from open. If a user selects any item in the list, they will be redirected to the
// StockDetails fragment which shows a much more detailed view of the stock.
public class ListFragment extends Fragment implements ListClickListener{
    private final String TAG = "LIST_FRAGMENT";
    private RecyclerView listRclView;
    private ListArrayAdapter adapter;

    private StockDatabase stockDB;

    private List<String> symbolList;
    private List<PriceQuickView> stockList;

    private Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockDB = StockDatabase.getInstance(getContext());
        symbolList = stockDB.stockDAO().getSymbolList();
        stockList = stockDB.stockDAO().getCurrentFinancialInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        listRclView = root.findViewById(R.id.rclView);
        listRclView.setLayoutManager(new LinearLayoutManager(root.getContext()));

        // Check the price of every stock in the list every 5 seconds
        handler = new Handler();
        final int delay = 5000;
        handler.postDelayed(new Runnable() {
            public void run() {
                // For each stock item in list
                for (String symbol : symbolList){
                    // New API call to check price
                    APICall call = new APICall(getContext());
                    call.GetFinancialInfo(symbol);
                    Log.wtf(TAG, "CHECK_" + symbol + "_CURRENT_PRICE");
                }

                handler.postDelayed(this, delay);
            }
        }, delay);

        DisplayRecyclerView(stockList);

        // Set up observer for price changes in database
        LiveData priceLiveData = stockDB.stockDAO().monitorChanges();
        priceLiveData.observe(getActivity(), observer);

        return root;
    }

    // Observer that monitors changes to database. If any item in database is changed,
    // the new data in the recyclerview is updated and redisplayed.
    final Observer<List<PriceQuickView>> observer = new Observer<List<PriceQuickView>>() {
        @Override
        public void onChanged(List<PriceQuickView> currentQuoteShort) {
            Log.wtf(TAG , "LiveData update from DB");
            DisplayRecyclerView(currentQuoteShort);
        }
    };


    // Method to display recyclerview on demand
    private void DisplayRecyclerView(List<PriceQuickView> stockList){
        adapter = new ListArrayAdapter(getContext(), stockList, this::listItemClicked);
        listRclView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // When list item is clicked, redirect user to StockDetails fragment of selected stock
    @Override
    public void listItemClicked(PriceQuickView quote){
        Stock stock = stockDB.stockDAO().getStockBySymbol(quote.getSymbol());
        FinancialInfo fInfo = stockDB.stockDAO().getFinancialInfoBySymbol(quote.getSymbol());
        CompanyInfo cInfo = stockDB.stockDAO().getCompanyInfoBySymbol(quote.getSymbol());
        stock.setFinancialInfo(fInfo);
        stock.setCompanyInfo(cInfo);

        Bundle bundle = new Bundle();
        bundle.putParcelable("Stock", stock);

        StockDetailsFragment fragment = new StockDetailsFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    // Stock and remove 5-second timer on fragment destruction
    // to stop timer triggering 5-second price check
    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}