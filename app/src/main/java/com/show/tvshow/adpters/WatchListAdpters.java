package com.show.tvshow.adpters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.show.tvshow.R;
import com.show.tvshow.dao.WatchListListener;
import com.show.tvshow.databinding.ItemContenerShowBinding;
import com.show.tvshow.lisners.TVShowsListener;
import com.show.tvshow.models.TVShow;

import java.util.List;


public class WatchListAdpters extends RecyclerView.Adapter<WatchListAdpters.TVShowViewHolder> {
    private LayoutInflater layoutInflater;
    private List<TVShow> tvShows;
    private WatchListListener watchListListener;


    public WatchListAdpters(List<TVShow> tvShows, WatchListListener watchListListener) {
        this.tvShows = tvShows;
        this.watchListListener=watchListListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ItemContenerShowBinding tvshowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_contener_show, parent, false
        );
        return new TVShowViewHolder(tvshowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }


    @Override
    public int getItemCount() {
        return tvShows.size();
    }

     class TVShowViewHolder extends RecyclerView.ViewHolder {

        private final ItemContenerShowBinding itemContenerShowBinding;

        public TVShowViewHolder(ItemContenerShowBinding itemContenerShowBinding) {
            super(itemContenerShowBinding.getRoot());
            this.itemContenerShowBinding = itemContenerShowBinding;
        }

        public void bindTVShow(TVShow tvShow) {
            itemContenerShowBinding.setTvShow(tvShow);
            itemContenerShowBinding.executePendingBindings();
            itemContenerShowBinding.getRoot().setOnClickListener(view -> {
             watchListListener.onTVShowClicked(tvShow);
            });

            itemContenerShowBinding.imageDelete.setOnClickListener(v -> watchListListener.removeTVShowFromWitchListener(tvShow,getAdapterPosition()));
            itemContenerShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }
    }

}
