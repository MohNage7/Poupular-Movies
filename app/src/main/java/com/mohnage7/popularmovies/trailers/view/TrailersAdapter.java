package com.mohnage7.popularmovies.trailers.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Trailers;
import com.mohnage7.popularmovies.utils.Constants;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohnage7 on 2/28/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ReviewsViewHolder> {
    private List<Trailers> mTrailersList;
    private Context mContext;

    TrailersAdapter(List<Trailers> mTrailersList, Context mContext) {
        this.mTrailersList = mTrailersList;
        this.mContext = mContext;
    }

    void updateReviewsAdapter(List<Trailers> reviewsList) {
        this.mTrailersList = reviewsList;
        notifyDataSetChanged();
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsViewHolder holder, int position) {
        final Trailers trailer = mTrailersList.get(position);
        // show loading for every video
        holder.loadingIndicatorView.smoothToShow();
        // set video title
        holder.trailerTitleTxtView.setText(trailer.getName());
        // load video thumbnail
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                holder.playButton.setVisibility(View.GONE);
                holder.loadingIndicatorView.smoothToHide();
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.playButton.setVisibility(View.VISIBLE);
                holder.loadingIndicatorView.smoothToHide();
            }
        };
        // init player
        holder.youTubeThumbnailView.initialize(Constants.YOUTUBE_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(trailer.getKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

        // play video in full screen activity when user clicks
        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) mContext, Constants.YOUTUBE_KEY, trailer.getKey());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailersList.size();
    }


    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView youTubeThumbnailView;
        @BindView(R.id.play_button)
        ImageButton playButton;
        @BindView(R.id.loading_view)
        AVLoadingIndicatorView loadingIndicatorView;
        @BindView(R.id.tailer_title_txt_view)
        TextView trailerTitleTxtView;

        ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
