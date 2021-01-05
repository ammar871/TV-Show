package com.show.tvshow.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.show.tvshow.models.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface TVShowDao {
    @Query("SELECT * FROM tvShows")
    Flowable<List<TVShow> >getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchList(TVShow tvShow);



    @Delete
    Completable removeFromWatchList(TVShow mUser);

    @Query("SELECT * FROM tvShows WHERE id = :tvShowId")
    Flowable<TVShow> getTVShowFromWatchList(String tvShowId);

}
