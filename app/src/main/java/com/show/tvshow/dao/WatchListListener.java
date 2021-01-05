package com.show.tvshow.dao;

import com.show.tvshow.models.TVShow;

public interface WatchListListener {

    void onTVShowClicked(TVShow tvShow);
    void removeTVShowFromWitchListener(TVShow tvShow,int position);
}
