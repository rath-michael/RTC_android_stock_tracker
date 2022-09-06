package com.example.capstoneproject.UI.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.Entity.Stock.PriceQuickView;
import com.example.capstoneproject.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ListArrayAdapter extends RecyclerView.Adapter<ListArrayAdapter.handler>{
    private static final String TAG = "LIST_ADAPTER";
    private Context context;
    private ListClickListener listener = null;
    private List<PriceQuickView> stockList;

    public ListArrayAdapter(Context context, List<PriceQuickView> stockList, ListClickListener listener){
        this.context = context;
        this.stockList = stockList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_recycler_view_single_row, parent, false);
        return new handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull handler holder, int position) {
        PriceQuickView stock = stockList.get(position);

        holder.txtSymbol.setText(stock.getSymbol());

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        String price = format.format(stock.getPrice());
        holder.txtCurrPrice.setText(price);

        String percChange = String.format("%.2f", stock.getChangePercentage());
        holder.txtPercentChange.setText(percChange + "%");
        if (stock.getChangePercentage() >= 0){
            holder.txtPercentChange.setTextColor(ContextCompat.getColor(context, R.color.line_green));
        }
        else if (stock.getChangePercentage() < 0){
            holder.txtPercentChange.setTextColor(ContextCompat.getColor(context, R.color.line_red));
        }
    }

    @Override
    public int getItemCount(){
        return stockList.size();
    }

    class handler extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtSymbol, txtCurrPrice, txtPercentChange;

        public handler(@NonNull View itemView) {
            super(itemView);
            txtSymbol = itemView.findViewById(R.id.txtSymbol);
            txtCurrPrice = itemView.findViewById(R.id.txtCurrentPrice);
            txtPercentChange = itemView.findViewById(R.id.txtPercentChange);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            listener.listItemClicked(stockList.get(getAdapterPosition()));
        }
    }
}
