package com.example.capstoneproject.Entity.News;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class News {
    @SerializedName("data")
    private List<News.Article> articles;

    public List<News.Article> getArticles(){
        return this.articles;
    }

    public class Article{
        @SerializedName("title")
        private String title;

        @SerializedName("url")
        private String url;

        @SerializedName("image_url")
        private String imageUrl;

        @SerializedName("published_at")
        private Date publishedAt;

        @SerializedName("source")
        private String source;

        public String getTitle() {
            return title;
        }

        public String getUrl() { return url; }

        public String getImageUrl() {
            return imageUrl;
        }

        public Date getPublishedAt() {
            return publishedAt;
        }

        public String getSource() {
            return source;
        }
    }
}
