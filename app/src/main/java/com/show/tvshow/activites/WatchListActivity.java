package com.show.tvshow.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.show.tvshow.R;
import com.show.tvshow.adpters.WatchListAdpters;
import com.show.tvshow.dao.WatchListListener;
import com.show.tvshow.databinding.ActivityWatchListBinding;
import com.show.tvshow.models.TVShow;

import com.show.tvshow.utilities.TempDataHolder;
import com.show.tvshow.viewModles.WatchingListViewModel;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchListListener {
    ActivityWatchListBinding activityWatchListBinding;
    private WatchingListViewModel viewModel;

    WatchListAdpters watchListAdpters;
    List<TVShow> watchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);

        doInitialization();
    }

    private void doInitialization() {

        viewModel = new ViewModelProvider(this).get(WatchingListViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(v -> onBackPressed());
        watchList=new ArrayList<>();
        loadWatchList();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (TempDataHolder.IS_WATCHLIST_UPDATE){
            loadWatchList();
            TempDataHolder.IS_WATCHLIST_UPDATE=false;
        }

    }

    private void loadWatchList() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatching().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                  if (watchList.size()>0){

                      watchList.clear();
                  }
                  watchList.addAll(tvShows);
                  watchListAdpters=new WatchListAdpters(watchList,this);
                  activityWatchListBinding.watchRecycler.setAdapter(watchListAdpters);
                  activityWatchListBinding.watchRecycler.setVisibility(View.VISIBLE);
                  compositeDisposable.dispose();

                }));
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent=new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWitchListener(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.removeTVShowFromWitchList(tvShow).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchList.remove(position);
                    watchListAdpters.notifyItemRemoved(position);
                    compositeDisposable.dispose();

                }));
    }
}