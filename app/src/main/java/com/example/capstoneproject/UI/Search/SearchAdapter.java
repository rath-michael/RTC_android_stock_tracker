package com.example.capstoneproject.UI.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.Entity.Stock.StockSearchItem;
import com.example.capstoneproject.R;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.handler>{
    private static final String TAG = "SEARCH_ADAPTER";
    private List<StockSearchItem> searchList;
    private SearchClickListener listener;

    public SearchAdapter(List<StockSearchItem> results, SearchClickListener listener){
        this.searchList = results;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchAdapter.handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.search_recycler_view_single_row, parent, false);
        return new SearchAdapter.handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.handler holder, int position) {
        holder.txtSymbol.setText(searchList.get(position).getSymbol());
        holder.txtName.setText(searchList.get(position).getName());
    }

    @Override
    public int getItemCount(){
        return searchList.size();
    }

    public class handler extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtSymbol, txtName;

        public handler(@NonNull View itemView) {
            super(itemView);
            txtSymbol = itemView.findViewById(R.id.txtSymbol);
            txtName = itemView.findViewById(R.id.txtName);
            txtName.setSelected(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            listener.searchItemClicked(searchList.get(getAdapterPosition()));
        }
    }
}
