package com.mohnage7.popularmovies.trailers.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Trailers;
import com.mohnage7.popularmovies.trailers.TrailersContract;
import com.mohnage7.popularmovies.trailers.presenter.TrailersPresenter;
import com.mohnage7.popularmovies.utils.InternetConnection;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.popularmovies.utils.Constants.MOVIE_ID;

public class TrailersActivity extends AppCompatActivity implements TrailersContract.IReviewsView {


    @BindView(R.id.trailers_recycler_view)
    RecyclerView mTrailersRecycler;
    @BindView(R.id.loading_view)
    AVLoadingIndicatorView loadingIndicatorView;
    private List<Trailers> mTrailersList;
    private TrailersAdapter mTrailersAdapter;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);
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
        getTrailersListFromServer();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }


    private void setupRecyclerView() {
        // create empty list
        mTrailersList = new ArrayList<>();
        //create adapter
        mTrailersAdapter = new TrailersAdapter(mTrailersList, this);
        // create layout manger
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // set layout manger
        mTrailersRecycler.setLayoutManager(layoutManager);
        // set movies adapter
        mTrailersRecycler.setAdapter(mTrailersAdapter);
    }


    private void getMoviesData() {
        // get movie id from intent
        movieId = getIntent().getIntExtra(MOVIE_ID, 0);
    }


    private void getTrailersListFromServer() {
        if (InternetConnection.isNetworkAvailable(this)) {
            // get presenter reference
            TrailersPresenter trailersPresenter = new TrailersPresenter(this);
            // make service call
            trailersPresenter.getTrailers(movieId);

        } else {
            // alert user with message
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            // hide loading dialog
            loadingIndicatorView.smoothToHide();
        }
    }


    @Override
    public void SetTrailers(List<Trailers> reviewsList) {
        // hide loading dialog
        loadingIndicatorView.hide();
        this.mTrailersList = reviewsList;
        // update adapter with the new data
        mTrailersAdapter.updateReviewsAdapter(mTrailersList);
    }

    @Override
    public void onFailure() {
        // hide loading dialog
        loadingIndicatorView.hide();
        // alert user with message
        Toast.makeText(this, R.string.movies_failure, Toast.LENGTH_SHORT).show();
    }

}
