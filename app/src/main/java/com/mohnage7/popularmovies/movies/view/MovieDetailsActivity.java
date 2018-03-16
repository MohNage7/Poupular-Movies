package com.mohnage7.popularmovies.movies.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.db.MovieContract;
import com.mohnage7.popularmovies.model.Movie;
import com.mohnage7.popularmovies.reviews.view.ReviewsActivity;
import com.mohnage7.popularmovies.trailers.view.TrailersActivity;
import com.mohnage7.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mohnage7.popularmovies.utils.Constants.MOVIE;
import static com.mohnage7.popularmovies.utils.Constants.MOVIE_ID;
import static com.mohnage7.popularmovies.utils.Constants.MOVIE_URL;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_title_txt_view)
    TextView titleTxtView;
    @BindView(R.id.movie_description)
    TextView descTxtView;
    @BindView(R.id.movie_poster_img_view)
    ImageView posterImageView;
    @BindView(R.id.movie_rating)
    TextView movieRatingTxtView;
    @BindView(R.id.movie_date_txt_view)
    TextView movieReleaseDatetxtView;
    @BindView(R.id.favorite_txt_view)
    TextView favoriteTxtView;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.movie_details_old);
        // inject views
        ButterKnife.bind(this);
        // get movie object
        getMovieFromIntent();
        // fill views with data
        setViews();
    }

    private void setViews() {
        titleTxtView.setText(movie.getTitle());
        descTxtView.setText(movie.getOverview());
        movieRatingTxtView.setText(String.valueOf(movie.getVoteAverage()));
        movieReleaseDatetxtView.setText(getString(R.string.release_date, movie.getReleaseDate()));
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + movie.getPosterPath()).into(posterImageView);
        setFavoriteIconColor();
    }

    /**
     * this method checks if the movie is added as a favorite before or not
     * and sets relevant favorite icon
     */
    private void setFavoriteIconColor() {
        if (checkIfMovieNotAddedBefore())
            favoriteTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_white_24dp, 0, 0);
        else
            favoriteTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_red_24dp, 0, 0);

    }

    /**
     * this method gets movie object from coming intent
     */
    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if (intent != null)
            movie = intent.getParcelableExtra(MOVIE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // exits activity with image transaction animation
        supportFinishAfterTransition();
    }


    @OnClick(R.id.reviews_txt_view)
    public void startReviewsActivity() {
        Intent intent = new Intent(this, ReviewsActivity.class);
        intent.putExtra(MOVIE_ID, movie.getId());
        intent.putExtra(MOVIE_URL, movie.getPosterPath());
        // init pairs
        Pair<View, String> posterPair = Pair.create((View) posterImageView, getString(R.string.poster));

        ActivityOptionsCompat imageTransitionOptions = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, posterPair);
        startActivity(intent, imageTransitionOptions.toBundle());
    }

    @OnClick(R.id.trailers_txt_view)
    public void startTrailersActivity() {
        Intent intent = new Intent(this, TrailersActivity.class);
        intent.putExtra(MOVIE_ID, movie.getId());
        startActivity(intent);
    }

    /**
     * this action method adds movie as favorite if it's not added before
     * and deletes movie from favorite if it's added before
     */
    @OnClick(R.id.favorite_txt_view)
    public void onFavoriteClickListener() {
        if (checkIfMovieNotAddedBefore())
            addMovieAsFavorite();
        else
            deleteMovieFromFavorite();
    }


    /**
     * this method adds the movie to the db if it doesn't exists and notify user
     */
    private void addMovieAsFavorite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(MovieContract.MovieEntry.COLUMN_COVER, movie.getBackdropPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        // update UI
        if (uri != null)
            updateFavoriteUi(true);
        else
            Toast.makeText(this, R.string.favorite_add_fail, Toast.LENGTH_SHORT).show();


    }

    /**
     * this method deletes movie from favorite db
     */
    private void deleteMovieFromFavorite() {
        int rowNum = getContentResolver().delete(MovieContract.MovieEntry.buildMovieUriWithDate(movie.getId()), null, null);
        if (rowNum != 0)
            updateFavoriteUi(false);
    }

    /**
     * @return true if the movie doesn't added before in the db and false otherwise
     */

    private boolean checkIfMovieNotAddedBefore() {
        // get movie from content provider by it's id
        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUriWithDate(movie.getId()), null, null, null, null, null);
        // if cursor !=null and cursor count == 0 return true otherwise false
        return (cursor != null ? cursor.getCount() : 0) == 0;
    }


    /**
     * this method change icon according to add or delete action and display toast for the user
     *
     * @param isAdded this field defines the action that method will take
     */
    private void updateFavoriteUi(boolean isAdded) {
        int message;
        // change button color
        if (isAdded) {
            message = R.string.favorite_add_sucess;
            favoriteTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_red_24dp, 0, 0);
        } else {
            message = R.string.deleted_from_favorite;
            favoriteTxtView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_favorite_white_24dp, 0, 0);
        }
        // notify user
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
