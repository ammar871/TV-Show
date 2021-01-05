package com.show.tvshow.viewModles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.show.tvshow.repositonis.MostPoplarTVShowsRepository;
import com.show.tvshow.response.TVShowResponse;

public class MostPoplarTVShowViewModel extends ViewModel {

    private MostPoplarTVShowsRepository mostPoplarTVShowsRepository;

    public MostPoplarTVShowViewModel (){
        mostPoplarTVShowsRepository=new MostPoplarTVShowsRepository();
    }

    public LiveData<TVShowResponse>getMostPoplarTVShow(int page){
        return mostPoplarTVShowsRepository.getMostPoplarTVShow(page);
    }

}
