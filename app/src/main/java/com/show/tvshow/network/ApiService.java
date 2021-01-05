package com.show.tvshow.network;

import com.show.tvshow.response.TVShowDetailsResponse;
import com.show.tvshow.response.TVShowResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<TVShowResponse>getMostPoplarTVShows(@Query("page")int page);

    @GET("show-details")
    Call<TVShowDetailsResponse>getTVShowDetails(@Query("q")String tvShowId);


    @GET("search")
    Call<TVShowResponse>searchTVShow(@Query("q")String tvShowId,@Query("page")int page);


}
