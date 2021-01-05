package com.show.tvshow.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.show.tvshow.R;
import com.show.tvshow.adpters.TVShowAdpters;

import com.show.tvshow.databinding.ActivitySearchBinding;
import com.show.tvshow.lisners.TVShowsListener;
import com.show.tvshow.models.TVShow;

import com.show.tvshow.viewModles.SearchViewModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {
ActivitySearchBinding activitySearchBinding;
    private SearchViewModel viewModel;

    private final List<TVShow> tvShows = new ArrayList<>();
    TVShowAdpters tvShowAdpters;

    private int currentPage = 1;
    private int totalPages = 1;

    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialization();

    }

    private void doInitialization() {


        activitySearchBinding.imageBack.setOnClickListener(v -> onBackPressed());

        activitySearchBinding.tvshowingrv.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        tvShowAdpters = new TVShowAdpters(tvShows, this);
        activitySearchBinding.tvshowingrv.setAdapter(tvShowAdpters);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
           if (timer!=null)
               timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()){
                    timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage=1;
                                totalPages=1;
                                searchTVSHow(s.toString());
                            });
                        }
                    },800);
                }else {
                    tvShows.clear();
                    tvShowAdpters.notifyDataSetChanged();
                }

            }
        });
        activitySearchBinding.tvshowingrv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.tvshowingrv.canScrollHorizontally(1)) {
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()){
                        if (currentPage<totalPages){
                            currentPage+=1;
                            searchTVSHow(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
    }

    private void searchTVSHow(String query){
        toggleLoading();
        viewModel.searchTVShow(query,currentPage).observe(this,tvShowResponse -> {
            toggleLoading();
            if (tvShowResponse != null) {
                totalPages = tvShowResponse.getTotalPage();
                if (tvShowResponse.getTvShows() != null) {
                   int oldCount=tvShows.size();
                   tvShows.addAll(tvShowResponse.getTvShows());
                   tvShowAdpters.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }

        });
    }

    private void toggleLoading() {

        if (currentPage == 1) {
            if (activitySearchBinding.getIsLoading() != null && activitySearchBinding.getIsLoading()) {
                activitySearchBinding.setIsLoading(false);

            } else {
                activitySearchBinding.setIsLoading(true);
            }
        } else {
            if (activitySearchBinding.getIsLoadingMore() != null && activitySearchBinding.getIsLoadingMore()) {
                activitySearchBinding.setIsLoadingMore(false);
            } else {
                activitySearchBinding.setIsLoadingMore(true);
            }


        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent=new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }
}