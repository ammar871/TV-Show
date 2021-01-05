package com.show.tvshow.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.show.tvshow.R;
import com.show.tvshow.adpters.EpisodesAdapter;
import com.show.tvshow.adpters.ImageSliderAdapter;
import com.show.tvshow.adpters.WatchListAdpters;
import com.show.tvshow.databinding.ActivityTVShowDetailsBinding;
import com.show.tvshow.databinding.LayoutEpisdsosBottomShetBinding;
import com.show.tvshow.models.TVShow;
import com.show.tvshow.utilities.TempDataHolder;
import com.show.tvshow.viewModles.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTVShowDetailsBinding binding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog bottomSheetDialog;
    private LayoutEpisdsosBottomShetBinding layoutEpisdsosBottomShetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableInWatchList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_t_v_show_details);
        doInitialization();


    }

    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowDetails();
        getTVShowDetails();

    }

    private void checkTVShowDetails() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchList(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    isTVShowAvailableInWatchList = true;
                    binding.imgWatchList.setImageResource(R.drawable.ic_added);
                    compositeDisposable.dispose();

                }));
    }

    private void getTVShowDetails() {


        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            binding.setIsLoading(false);
            if (tvShowDetailsResponse.getTvShowDetails() != null) {
                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImagesSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }
                binding.setTvShowImageUrl(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                binding.imageTvShow.setVisibility(View.VISIBLE);
                binding.setDescription(String.valueOf(HtmlCompat.fromHtml(tvShowDetailsResponse.getTvShowDetails().getDescription(),
                        HtmlCompat.FROM_HTML_MODE_LEGACY)));
                binding.textDescription.setVisibility(View.VISIBLE);
                binding.texreadMor.setVisibility(View.VISIBLE);
                binding.texreadMor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (binding.texreadMor.getText().toString().equals("Read More..")) {
                            binding.textDescription.setMaxLines(Integer.MAX_VALUE);
                            binding.textDescription.setEllipsize(null);
                            binding.texreadMor.setText(R.string.read_less);
                        } else {
                            binding.textDescription.setMaxLines(4);
                            binding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                            binding.texreadMor.setText(R.string.read_more);
                        }
                    }
                });

                binding.setRating(String.format(Locale.getDefault(),
                        "%.2f", Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())));
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    binding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    binding.setGenre("N/A");
                }
                binding.setRating(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                binding.viewdivider1.setVisibility(View.VISIBLE);
                binding.layoumaisc.setVisibility(View.VISIBLE);
                binding.viewdivider2.setVisibility(View.VISIBLE);

                binding.buttunWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                        startActivity(intent);
                    }
                });
                binding.buttunWebsite.setVisibility(View.VISIBLE);
                binding.buttunepisodesw.setVisibility(View.VISIBLE);

                binding.buttunepisodesw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bottomSheetDialog == null) {
                            bottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                            layoutEpisdsosBottomShetBinding = DataBindingUtil.inflate(
                                    LayoutInflater.from(TVShowDetailsActivity.this)
                                    , R.layout.layout_episdsos_bottom_shet, findViewById(R.id.espiodesCountener), false
                            );
                            bottomSheetDialog.setContentView(layoutEpisdsosBottomShetBinding.getRoot());
                            layoutEpisdsosBottomShetBinding.episodesRv.setAdapter(
                                    new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                            );
                            layoutEpisdsosBottomShetBinding.textTitle.setText(String.format("Episodes | %s",
                                    tvShow.getName()));
                        }
                        layoutEpisdsosBottomShetBinding.imgclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });
                        //-----optional section-------//
                        FrameLayout frameLayout = bottomSheetDialog.findViewById(
                                com.google.android.material.R.id.design_bottom_sheet
                        );
                        if (frameLayout != null) {
                            BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                            bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        //-----optional section-------//
                        bottomSheetDialog.show();
                    }
                });
                binding.imgWatchList.setOnClickListener(view -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            if (isTVShowAvailableInWatchList) {
                                new CompositeDisposable().add(tvShowDetailsViewModel.removeTVShowFromWatchList(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTVShowAvailableInWatchList = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATE=true;
                                            binding.imgWatchList.setImageResource(R.drawable.ic_wachliste_24);
                                            Toast.makeText(TVShowDetailsActivity.this, "Removed from watchList", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                            } else {

                                compositeDisposable.add(tvShowDetailsViewModel.addToWatchList(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATE=true;
                                            binding.imgWatchList.setImageResource(R.drawable.ic_added);
                                            Toast.makeText(TVShowDetailsActivity.this, "Added to watchList", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));


                            }


                        }


                );
                binding.imgWatchList.setVisibility(View.VISIBLE);
                loadBasicTVShowDetails();
            }
        });
    }

    private void loadImagesSlider(String[] sliderImages) {
        binding.sliderViewPager.setOffscreenPageLimit(1);
        binding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.sliderViewPager.setVisibility(View.VISIBLE);
        binding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicator(sliderImages.length);
        binding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    private void setupSliderIndicator(int count) {

        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.backround_slider_incetor_inactive)
            );
            indicators[i].setLayoutParams(layoutParams);
            binding.layoutsliderInicators.addView(indicators[i]);
        }
        binding.layoutsliderInicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);
    }

    private void setCurrentSliderIndicator(int position) {
        int childCont = binding.layoutsliderInicators.getChildCount();
        for (int i = 0; i < childCont; i++) {
            ImageView imageView = (ImageView) binding.layoutsliderInicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.backround_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.backround_slider_incetor_inactive));

            }

        }
    }


    private void loadBasicTVShowDetails() {
        binding.setTVShowName(tvShow.getName());
        binding.setNetWorkCountry(tvShow.getNetwork() + "(" +
                tvShow.getCountry());
        binding.setStatus(tvShow.getStatus());
        binding.setStartDate(tvShow.getStartDate());

        binding.txtNmae.setVisibility(View.VISIBLE);
        binding.txtnetWork.setVisibility(View.VISIBLE);
        binding.txtstatus.setVisibility(View.VISIBLE);
        binding.txtstartDate.setVisibility(View.VISIBLE);


    }
}