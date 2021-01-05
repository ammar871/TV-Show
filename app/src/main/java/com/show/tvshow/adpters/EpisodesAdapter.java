package com.show.tvshow.adpters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.show.tvshow.R;
import com.show.tvshow.databinding.ItemContenerShowBinding;
import com.show.tvshow.databinding.ItemEpisodeCountainerBinding;
import com.show.tvshow.lisners.TVShowsListener;
import com.show.tvshow.models.Episode;
import com.show.tvshow.models.TVShow;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    private List<Episode> episodes;
    private TVShowsListener tvShowsListener;

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ItemEpisodeCountainerBinding tvshowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_episode_countainer, parent, false
        );
        return new EpisodeViewHolder(tvshowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindTVShow(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private final ItemEpisodeCountainerBinding itemContenerShowBinding;

        public EpisodeViewHolder(ItemEpisodeCountainerBinding itemContenerShowBinding) {
            super(itemContenerShowBinding.getRoot());
            this.itemContenerShowBinding = itemContenerShowBinding;
        }

        public void bindTVShow(Episode episode) {

            String title="5";
            String season=episode.getSeason();
            if (season.length() == 1) {
                season="0".concat(season);
            }
            String episodeNumber=episode.getEpisode();
            if (episodeNumber.length() == 1) {
                episodeNumber="0".concat(episodeNumber);
            }
            episodeNumber="E".concat(episodeNumber);
            title=title.concat(season).concat(episodeNumber);
            itemContenerShowBinding.setTitle(title);
            itemContenerShowBinding.setName(episode.getName());
            itemContenerShowBinding.setAirDate(episode.getAirDate());
            }

        }
    }

