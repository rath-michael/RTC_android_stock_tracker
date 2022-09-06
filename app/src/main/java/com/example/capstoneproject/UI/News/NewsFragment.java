package com.example.capstoneproject.UI.News;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstoneproject.Database.StockDatabase;
import com.example.capstoneproject.Entity.News.News;
import com.example.capstoneproject.R;
import com.example.capstoneproject.Service.APICall;

// Fragment to display news information about all stocks available from API provider
public class NewsFragment extends Fragment implements NewsLoader{
    private static final String TAG = "NEWS_FRAGMENT";
    private ConstraintLayout progressBar;
    private RecyclerView rclView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        rclView = root.findViewById(R.id.rclView);

        // API call to retrieve news items
        APICall call = new APICall(getContext());
        call.GetNews("", this::onNewsLoaded);

        return root;
    }

    // Receive news data from API call class and assign to news recyclerview
    @Override
    public void onNewsLoaded(News newsList){
        if (newsList != null){
            NewsAdapter adapter = new NewsAdapter(newsList.getArticles());
            rclView.setLayoutManager(new LinearLayoutManager(getContext()));
            rclView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);
        }
    }
}