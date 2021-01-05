package com.show.tvshow.viewModles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.show.tvshow.database.TVShowDatabase;
import com.show.tvshow.models.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchingListViewModel extends AndroidViewModel {
    TVShowDatabase tvShowDatabase;
    public WatchingListViewModel(@NonNull Application application) {
        super(application);
        tvShowDatabase=TVShowDatabase.getDatabaseInstance(application);

    }

    public Flowable<List<TVShow>> loadWatching(){
        return tvShowDatabase.tvShowDao().getAll();
    }


    public Completable removeTVShowFromWitchList(TVShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);

    }
}
