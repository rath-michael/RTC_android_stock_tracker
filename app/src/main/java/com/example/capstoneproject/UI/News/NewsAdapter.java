package com.example.capstoneproject.UI.News;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.Entity.News.News;
import com.example.capstoneproject.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.handler>{
    private static final String TAG = "NEWS_ADAPTER";
    private List<News.Article> articleList;

    public NewsAdapter(List<News.Article> articleList){
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsAdapter.handler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news_recycler_view_single_row, parent, false);
        return new handler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull handler holder, int position) {
        holder.txtSource.setText(articleList.get(position).getSource());
        holder.txtTitle.setText(articleList.get(position).getTitle());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String pubDate = format.format(articleList.get(position).getPublishedAt());
        holder.txtPublishDate.setText(pubDate);

        Picasso.get().load(articleList.get(position).getImageUrl()).into(holder.imgView);
    }

    @Override
    public int getItemCount(){
        return articleList.size();
    }

    class handler extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtSource, txtTitle, txtPublishDate;
        ImageView imgView;

        public handler(@NonNull View itemView) {
            super(itemView);
            txtSource = itemView.findViewById(R.id.txtSource);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPublishDate = itemView.findViewById(R.id.txtPublishedDate);
            imgView = itemView.findViewById(R.id.imgView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(articleList.get(getAdapterPosition()).getUrl()));
            view.getContext().startActivity(browserIntent);
        }
    }
}
