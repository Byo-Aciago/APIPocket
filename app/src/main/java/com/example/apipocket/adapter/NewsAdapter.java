package com.example.apipocket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apipocket.R;
import com.example.apipocket.model.news.NewsArticle;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsArticle> articleList;

    public NewsAdapter(List<NewsArticle> articleList) {
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articleList.get(position);
        if (article != null) {
            holder.tvTitle.setText(article.getTitle() != null ? article.getTitle() : "Sin título");

            String sourceName = (article.getSource() != null && article.getSource().getName() != null)
                    ? article.getSource().getName()
                    : "Noticias";
            String date = article.getPublishedAt() != null ? article.getPublishedAt().split("T")[0] : "";
            holder.tvSourceDate.setText(sourceName + (date.isEmpty() ? "" : " • " + date));

            if (article.getDescription() != null && !article.getDescription().isEmpty()) {
                holder.tvDescription.setText(article.getDescription());
                holder.tvDescription.setVisibility(View.VISIBLE);
            } else {
                holder.tvDescription.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvSourceDate;
        TextView tvDescription;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvNewsTitle);
            tvSourceDate = itemView.findViewById(R.id.tvNewsSourceDate);
            tvDescription = itemView.findViewById(R.id.tvNewsDescription);
        }
    }
}
