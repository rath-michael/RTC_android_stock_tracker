package com.example.capstoneproject.UI.Search;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.capstoneproject.Entity.Stock.CompanyInfo;
import com.example.capstoneproject.Entity.Stock.FinancialInfo;
import com.example.capstoneproject.Entity.Stock.Stock;
import com.example.capstoneproject.Entity.Stock.StockSearchItem;
import com.example.capstoneproject.R;
import com.example.capstoneproject.Service.APICall;
import com.example.capstoneproject.UI.Error.ErrorFragment;
import com.example.capstoneproject.UI.List.ListFragment;
import com.example.capstoneproject.UI.Stock_Details.CompanyInfoLoader;
import com.example.capstoneproject.UI.Stock_Details.FinancialInfoLoader;
import com.example.capstoneproject.UI.Stock_Details.StockDetailsFragment;

import java.util.List;

// Fragment to allow user to search for a specific stock. All items returned from search API call
// can be selected by the user, which will redirect the user to the StockDetails fragment for more
// specific details
public class SearchFragment extends Fragment implements SearchClickListener, FinancialInfoLoader, CompanyInfoLoader {
    private static final String TAG = "SEARCH_FRAGMENT";
    private Stock stock;
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rclView;
    private boolean financialLoaded, companyLoaded;
    private ConstraintLayout progressBar;
    private ConstraintLayout errorDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        errorDisplay = root.findViewById(R.id.errorDisplay);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etSearch = view.findViewById(R.id.etSearchText);
        btnSearch = view.findViewById(R.id.btnSearch);
        rclView = view.findViewById(R.id.rclView);

        // Button to start search API call with text entered by user
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorDisplay.setVisibility(View.GONE);
                String symbol = etSearch.getText().toString();

                // New API call
                APICall call = new APICall(getContext());
                call.SearchForTicker(symbol, this::onSymbolSearchLoaded);

                etSearch.setText(symbol);

                // Remove keyboard from screen
                InputMethodManager im = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            }

            // Receives response data from API call
            private void onSymbolSearchLoaded(List<StockSearchItem> result) {
                // If data isn't empty, send data to recyclerview to be displayed
                if (result != null){
                    SearchAdapter adapter = new SearchAdapter(result, SearchFragment.this::searchItemClicked);
                    rclView.setLayoutManager(new LinearLayoutManager(getContext()));
                    rclView.setAdapter(adapter);
                }
                // If data empty, display error
                else {
                    errorDisplay.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // List item selected, redirects user to StockDetails fragment
    @Override
    public void searchItemClicked(StockSearchItem searchItem){
        progressBar.setVisibility(View.VISIBLE);
        stock = new Stock(searchItem.getSymbol());

        // API calls for financial and company information about selected stock
        APICall call = new APICall(getContext());
        call.GetFinancialInfo(stock.getSymbol(), this::onFinancialInfoLoaded);
        call.GetCompanyInfo(stock.getSymbol(), this::onCompanyInfoLoaded);

        // Check every 1 second until financial and company info are loaded
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // If data load is completed, redirect user to StockDetails fragment
                if (financialLoaded && companyLoaded){

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Stock", stock);

                    StockDetailsFragment fragment = new StockDetailsFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    // Receive data from financial information API call
    @Override
    public void onFinancialInfoLoaded(FinancialInfo info){
        if (info != null){
            stock.setFinancialInfo(info);
        }
        financialLoaded = true;
    }

    // Receive data from company information API call
    @Override
    public void onCompanyInfoLoaded(CompanyInfo info){
        if (info != null){
            stock.setCompanyInfo(info);
        }
        companyLoaded = true;
    }
}