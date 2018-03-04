package com.mohnage7.popularmovies.view;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Movie;
import com.mohnage7.popularmovies.utils.Constants;
import com.mohnage7.popularmovies.widgets.SquareImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.popularmovies.utils.Constants.MOVIE;

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

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);

        getMovieFromIntent();

        setViews();


    }

    private void setViews() {
        titleTxtView.setText(movie.getTitle());
        descTxtView.setText(movie.getOverview());
        movieRatingTxtView.setText(String.valueOf(movie.getVoteAverage()));
        movieReleaseDatetxtView.setText(getString(R.string.release_date, movie.getReleaseDate()));
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + movie.getPosterPath()).into(posterImageView);
    }

    private void getMovieFromIntent() {
        Intent intent = getIntent();
        if (intent != null)
            movie = intent.getParcelableExtra(MOVIE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

}
