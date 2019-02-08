package com.example.sonakshi.socialcops.adapters;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.sonakshi.socialcops.R;
import com.example.sonakshi.socialcops.databinding.NewsItemBinding;
import com.example.sonakshi.socialcops.models.Article;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final SearchAdapter.SearchAdapterListener listener;
    private List<Article> articles;
    private LayoutInflater layoutInflater;

    public SearchAdapter(List<Article> articles, SearchAdapter.SearchAdapterListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        NewsItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.news_item, parent, false);
        return new SearchAdapter.SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder newsViewHolder, int i) {
        newsViewHolder.binding.setNews(articles.get(i));
        newsViewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public void setArticles(List<Article> articles) {
        if (articles != null) {
            this.articles = articles;
            notifyDataSetChanged();
        }
    }

    public interface SearchAdapterListener {
        void onNewsItemClicked(Article article);

        void onItemOptionsClicked(Article article);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final NewsItemBinding binding;

        public SearchViewHolder(final NewsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.ivOptions.setOnClickListener(this);
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = this.getAdapterPosition();
            if (v instanceof ImageView) {
                listener.onItemOptionsClicked(articles.get(index));
            } else {
                listener.onNewsItemClicked(articles.get(index));
            }
        }
    }

}
