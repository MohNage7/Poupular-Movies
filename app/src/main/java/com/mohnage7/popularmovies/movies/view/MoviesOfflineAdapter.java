package com.mohnage7.popularmovies.movies.view;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.db.MovieContract;
import com.mohnage7.popularmovies.model.Movie;
import com.mohnage7.popularmovies.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohnage7 on 3/12/2018.
 */

public class MoviesOfflineAdapter extends RecyclerView.Adapter<MoviesOfflineAdapter.TaskViewHolder> {

    MoviesAdapter.OnMovieClickListener onMovieClickListener;
    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;

    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MoviesOfflineAdapter(Context mContext, MoviesAdapter.OnMovieClickListener onMovieClickListener) {
        this.mContext = mContext;
        this.onMovieClickListener = onMovieClickListener;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_item, parent, false);

        return new TaskViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(final TaskViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int imageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
        int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
        int overViewIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        int averageRateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
        int releaseDateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);

        mCursor.moveToPosition(position); // get to the right location in the cursor

        final Movie movie = new Movie();
        movie.setTitle(mCursor.getString(titleIndex));
        movie.setId(mCursor.getInt(idIndex));
        movie.setPosterPath(mCursor.getString(imageIndex));
        movie.setOverview(mCursor.getString(overViewIndex));
        movie.setVoteAverage(mCursor.getDouble(averageRateIndex));
        movie.setReleaseDate(mCursor.getString(releaseDateIndex));
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


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie, ImageView imageView, TextView movieTitle);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movie_img_view)
        ImageView movieImageView;
        @BindView(R.id.movie_title_txt_view)
        TextView movieTitle;

        TaskViewHolder(View itemView) {
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
                    .into(movieImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(mContext)
                                    .load(imageUrl)
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