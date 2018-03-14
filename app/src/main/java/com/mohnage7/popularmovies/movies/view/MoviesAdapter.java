package com.mohnage7.popularmovies.movies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Movie;
import com.mohnage7.popularmovies.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohnage7 on 2/28/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {
    private List<Movie> mMoviesList;
    private Context mContext;
    private OnMovieClickListener onMovieClickListener;

    MoviesAdapter(List<Movie> moviesList, Context context, OnMovieClickListener onMovieClickListener) {
        mMoviesList = moviesList;
        mContext = context;
        this.onMovieClickListener = onMovieClickListener;
    }


    void updateMoviesAdapter(List<Movie> moviesList) {
        this.mMoviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesViewHolder holder, int position) {
        final Movie movie = mMoviesList.get(position);
        // set movie title
        holder.movieTitle.setText(movie.getTitle());
        // set movie image
        holder.loadImage(Constants.IMAGE_BASE_URL + movie.getPosterPath());
        // on movie clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMovieClickListener.onMovieClick(movie, holder.movieImageView, holder.movieTitle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie, ImageView imageView, TextView movieTitle);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img_view)
        ImageView movieImageView;
        @BindView(R.id.movie_title_txt_view)
        TextView movieTitle;

        MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * this method loads the image from Cache if it failed it load it again from the network
         *
         * @param imageUrl image to load
         */
        void loadImage(final String imageUrl) {
            Picasso.with(mContext)
                    .load(imageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.movie_place_holder)
                    .into(movieImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(mContext)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.movie_place_holder)
                                    .into(movieImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });
                        }
                    });
        }
    }
}
