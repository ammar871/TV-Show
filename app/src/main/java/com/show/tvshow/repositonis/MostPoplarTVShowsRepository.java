package com.show.tvshow.repositonis;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.show.tvshow.network.ApiClient;
import com.show.tvshow.network.ApiService;
import com.show.tvshow.response.TVShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPoplarTVShowsRepository {

    private ApiService apiService;

    public MostPoplarTVShowsRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);

    }

    public LiveData<TVShowResponse> getMostPoplarTVShow(int page) {
        MutableLiveData<TVShowResponse> data = new MutableLiveData<>();
        apiService.getMostPoplarTVShows(page).enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowResponse> call, @NonNull Response<TVShowResponse> response) {
                data.setValue(response.body());

            }

            @Override
            public void onFailure(@NonNull Call<TVShowResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
