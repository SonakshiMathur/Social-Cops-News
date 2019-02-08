package com.example.sonakshi.socialcops.ui.search;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;

import com.example.sonakshi.socialcops.R;
import com.example.sonakshi.socialcops.adapters.SearchAdapter;
import com.example.sonakshi.socialcops.databinding.FragmentSearchBinding;
import com.example.sonakshi.socialcops.models.Article;
import com.example.sonakshi.socialcops.models.Specification;
import com.example.sonakshi.socialcops.network.NewsApi;
import com.example.sonakshi.socialcops.ui.news.DetailActivity;
import com.example.sonakshi.socialcops.ui.news.NewsViewModel;
import com.example.sonakshi.socialcops.ui.news.OptionsBottomSheet;

import java.util.List;

public class SearchFragment extends Fragment implements SearchAdapter.SearchAdapterListener{

    FragmentSearchBinding binding;
    public static final String PARAM_LIST_STATE = "param-state";
    private SearchAdapter searchAdapter = new SearchAdapter(null, this);
    private Parcelable listState;

    public SearchFragment(){

    }
    public static SearchFragment newInstance(){
        return new SearchFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search, container, false);
        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(PARAM_LIST_STATE);
        }
        EditText searchEditText = (EditText) binding.search.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimaryLight));
        ViewCompat.setElevation(binding.appbarSearch, getResources().getDimension(R.dimen.tab_layout_elevation));
        final RecyclerView recyclerView = binding.recyclerSearch;
        recyclerView.setAdapter(searchAdapter);
        final Specification specs = new Specification();
        specs.setCategory(NewsApi.Category.all);
        final NewsViewModel viewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        final Fragment f = this;
        if (getActivity() != null) {
            binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    specs.setQ(s);
                    viewModel.getNewsHeadlines(specs).observe(f, new Observer<List<Article>>() {
                        @Override
                        public void onChanged(@Nullable List<Article> articles) {
                            if (articles != null) {
                                searchAdapter.setArticles(articles);

                                restoreRecyclerViewState();
                            }
                        }
                    });

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
            if(getContext()!=null) {
                DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                divider.setDrawable(getResources().getDrawable(R.drawable.recycler_view_divider));
                recyclerView.addItemDecoration(divider);
            }
        }
        return this.binding.getRoot();
    }

    @Override
    public void onNewsItemClicked(Article article) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(DetailActivity.PARAM_ARTICLE, article);
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        binding.recyclerSearch.setLayoutAnimation(controller);
        binding.recyclerSearch.scheduleLayoutAnimation();
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().overridePendingTransition(R.anim.slide_up_animation, R.anim.fade_exit_transition);
        }
    }

    @Override
    public void onItemOptionsClicked(Article article) {
        OptionsBottomSheet bottomSheet = OptionsBottomSheet.getInstance(article.getTitle(), article.getUrl(), article.getId(), false, 0);
        if (getActivity() != null) {
            bottomSheet.show(getActivity().getSupportFragmentManager(), bottomSheet.getTag());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (binding.recyclerSearch.getLayoutManager() != null) {
            listState = binding.recyclerSearch.getLayoutManager().onSaveInstanceState();
            outState.putParcelable(PARAM_LIST_STATE, listState);
        }
    }

    private void restoreRecyclerViewState() {
        if (binding.recyclerSearch.getLayoutManager() != null) {
            binding.recyclerSearch.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
