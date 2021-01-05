package com.show.tvshow.adpters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.show.tvshow.R;
import com.show.tvshow.databinding.ItemContenerShowBinding;
import com.show.tvshow.databinding.ItemContenerSliderImageBinding;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{

    private String[]sliderImages;
    private LayoutInflater layoutInflater;
    public ImageSliderAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ItemContenerSliderImageBinding tvshowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_contener_slider_image, parent, false
        );
        return new ImageSliderViewHolder(tvshowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
    holder.bindSliderImage(sliderImages[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        private ItemContenerSliderImageBinding itemContenerSliderImageBinding;
        public ImageSliderViewHolder(ItemContenerSliderImageBinding itemContenerSliderImageBinding) {
            super(itemContenerSliderImageBinding.getRoot());
            this.itemContenerSliderImageBinding=itemContenerSliderImageBinding;
        }

        public void bindSliderImage(String imageURL){
            itemContenerSliderImageBinding.setImageURL(imageURL);
        }
    }
}
