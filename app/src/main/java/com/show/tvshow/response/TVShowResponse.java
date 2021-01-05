package com.show.tvshow.response;

import com.google.gson.annotations.SerializedName;
import com.show.tvshow.models.TVShow;

import java.util.List;

public class TVShowResponse {


    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPage;

    @SerializedName("tv_shows")
    private List<TVShow>tvShows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }

    public void setTvShows(List<TVShow> tvShows) {
        this.tvShows = tvShows;
    }
}
