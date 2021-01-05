package com.show.tvshow.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;



import com.show.tvshow.R;
import com.show.tvshow.adpters.TVShowAdpters;
import com.show.tvshow.databinding.ActivityMainBinding;
import com.show.tvshow.lisners.TVShowsListener;
import com.show.tvshow.models.TVShow;
import com.show.tvshow.viewModles.MostPoplarTVShowViewModel;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowsListener {
    private MostPoplarTVShowViewModel viewModel;
    ActivityMainBinding binding;
    private final List<TVShow> tvShows = new ArrayList<>();
    TVShowAdpters tvShowAdpters;

    private int currentPage = 1;
    private int totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization() {
        binding.tvshowingrv.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPoplarTVShowViewModel.class);

        tvShowAdpters = new TVShowAdpters(tvShows, this);
        binding.tvshowingrv.setAdapter(tvShowAdpters);
        binding.tvshowingrv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!binding.tvshowingrv.canScrollHorizontally(1)) {
                    if (currentPage <= totalPages) {
                        currentPage += 1;
                        getMostPoplarTVShows();
                    }
                }
            }
        });
        binding.imgwachlist.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),WatchListActivity.class)));
        binding.imgsearsh.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SearchActivity.class)));
        getMostPoplarTVShows();

    }

    private void getMostPoplarTVShows() {
        toggleLoading();
        viewModel.getMostPoplarTVShow(currentPage).observe(this, mostPoplarTVShowRespons
                -> {
            toggleLoading();
            if (mostPoplarTVShowRespons != null) {
                totalPages = mostPoplarTVShowRespons.getTotalPage();
                if (mostPoplarTVShowRespons.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPoplarTVShowRespons.getTvShows());
                    tvShowAdpters.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }

    private void toggleLoading() {

        if (currentPage == 1) {
            if (binding.getIsLoading() != null && binding.getIsLoading()) {
                binding.setIsLoading(false);

            } else {
                binding.setIsLoading(true);
            }
        } else {
            if (binding.getIsLoadingMore() != null && binding.getIsLoadingMore()) {

                binding.setIsLoadingMore(false);
            } else {
                binding.setIsLoadingMore(true);
            }


        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);

        startActivity(intent);

    }
}