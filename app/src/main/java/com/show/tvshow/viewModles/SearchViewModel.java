package com.show.tvshow.viewModles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.show.tvshow.repositonis.SearchTVShowRepository;
import com.show.tvshow.response.TVShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel() {
        this.searchTVShowRepository = new SearchTVShowRepository();
    }
    public LiveData<TVShowResponse> searchTVShow(String query,int page){
        return searchTVShowRepository.searchTVShow(query,page);
    }

}
