package com.show.tvshow.viewModles;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.show.tvshow.database.TVShowDatabase;
import com.show.tvshow.models.TVShow;
import com.show.tvshow.models.TVShowDetails;
import com.show.tvshow.repositonis.MostPoplarTVShowsRepository;
import com.show.tvshow.repositonis.TVShowDetailsRepository;
import com.show.tvshow.response.TVShowDetailsResponse;
import com.show.tvshow.response.TVShowResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowDatabase tvShowDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TVShowDatabase.getDatabaseInstance(application);
    }

    public LiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchList(TVShow tvShow) {
        return tvShowDatabase.tvShowDao().addToWatchList(tvShow);
    }
    public Flowable<TVShow> getTVShowFromWatchList(String tvShowId){
        return tvShowDatabase.tvShowDao().getTVShowFromWatchList(tvShowId);
    }

    public Completable removeTVShowFromWatchList(TVShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchList(tvShow);
    }
}
