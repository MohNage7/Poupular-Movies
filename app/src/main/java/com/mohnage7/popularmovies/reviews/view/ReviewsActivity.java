package com.mohnage7.popularmovies.reviews.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Review;
import com.mohnage7.popularmovies.reviews.ReviewContract;
import com.mohnage7.popularmovies.reviews.presenter.ReviewsPresenter;
import com.mohnage7.popularmovies.utils.Constants;
import com.mohnage7.popularmovies.utils.InternetConnection;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.popularmovies.utils.Constants.MOVIE_ID;
import static com.mohnage7.popularmovies.utils.Constants.MOVIE_URL;

public class ReviewsActivity extends AppCompatActivity implements ReviewContract.IReviewsView {

    @BindView(R.id.reviews_recycler_view)
    RecyclerView mReviewsRecycler;
    @BindView(R.id.movie_poster_img_view)
    ImageView moviePosterImgView;
    @BindView(R.id.loading_view)
    AVLoadingIndicatorView loadingIndicatorView;
    private List<Review> mReviewsList;
    private ReviewsAdapter mReviewsAdapter;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        // bind views
        ButterKnife.bind(this);
        loadingIndicatorView.smoothToShow();

        // show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get movies data
        getMoviesData();
        // getting recycler ready
        setupRecyclerView();
        // get movies data from the server
        getReviewsListFromServer();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

    private void setupRecyclerView() {
        // create empty list
        mReviewsList = new ArrayList<>();
        //create adapter
        mReviewsAdapter = new ReviewsAdapter(mReviewsList);
        // create layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // set layout manger
        mReviewsRecycler.setLayoutManager(layoutManager);
        // set movies adapter
        mReviewsRecycler.setAdapter(mReviewsAdapter);
    }


    private void getMoviesData() {
        // get movie id from intent
        movieId = getIntent().getIntExtra(MOVIE_ID, 0);
        String movieUrl = getIntent().getStringExtra(MOVIE_URL);
        // set back ground image
        setMovieImage(movieUrl);
    }

    private void setMovieImage(String imageName) {
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + imageName).into(moviePosterImgView);
    }

    private void getReviewsListFromServer() {
        if (InternetConnection.isNetworkAvailable(this)) {
            // get presenter reference
            ReviewsPresenter reviewsPresenter = new ReviewsPresenter(this);
            // make service call
            reviewsPresenter.getReviews(movieId);

        } else {
            // alert user with message
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            // hide loading dialog
            loadingIndicatorView.smoothToHide();
        }
    }


    @Override
    public void setReviews(List<Review> reviewsList) {
        // hide loading dialog
        loadingIndicatorView.smoothToHide();
        this.mReviewsList = reviewsList;
        // update adapter with the new data
        mReviewsAdapter.updateReviewsAdapter(mReviewsList);
    }

    @Override
    public void onFailure() {
        // hide loading dialog
        loadingIndicatorView.smoothToHide();
        // alert user with message
        Toast.makeText(this, R.string.movies_failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
