package com.mohnage7.popularmovies.movies.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.db.MovieContract;
import com.mohnage7.popularmovies.model.Movie;
import com.mohnage7.popularmovies.movies.MoviesContract;
import com.mohnage7.popularmovies.movies.presenter.MoviesPresenter;
import com.mohnage7.popularmovies.utils.Constants;
import com.mohnage7.popularmovies.utils.InternetConnection;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.popularmovies.utils.Constants.FAVORITE;
import static com.mohnage7.popularmovies.utils.Constants.LIST_STATE;
import static com.mohnage7.popularmovies.utils.Constants.MOVIE;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.IMoviesView, MoviesAdapter.OnMovieClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String MOVIES_TYPE = "movies_type";
    private static final int MOVIE_LOADER_ID = 0;
    @BindView(R.id.loading_view)
    public AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.movies_recycler_view)
    RecyclerView mMoviesRecyclerView;
    @BindView(R.id.navigation_view)
    BottomNavigationView bottomNavigationView;
    private List<Movie> mMoviesList;
    private MoviesAdapter mMoviesAdapter;
    private int mSelectedMovieType = 0;
    private boolean isLoadedBefore;
    private GridLayoutManager layoutManager;
    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        // bind views
        ButterKnife.bind(this);

        // get movies type if exists
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_TYPE))
                mSelectedMovieType = savedInstanceState.getInt(MOVIES_TYPE);

        }

        setupBottomBar();
        // getting recycler ready
        setupRecyclerView();
        // get movies according to user selection with respect to configuration changes
        if (mSelectedMovieType == Constants.FAVORITE)
            restartLoader();
        else
            getMoviesListFromServer();

    }

    @Override
    protected void onResume() {
        super.onResume();

        loadingIndicatorView.hide();

        if (mSelectedMovieType == FAVORITE)
            restartLoader();

    }

    public void setupBottomBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bottom_popular:
                                mSelectedMovieType = Constants.POPULAR;
                                getMoviesListFromServer();
                                break;
                            case R.id.bottom_top_rated:
                                mSelectedMovieType = Constants.TOP_RATED;
                                getMoviesListFromServer();
                                break;
                            case R.id.bottom_favorite:
                                getMoviesListFromDatabase();
                                break;
                        }
                        return true;
                    }
                });
    }

    private void initLoader() {
        isLoadedBefore = true;
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    private void restartLoader() {
        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
    }


    private void getMoviesListFromDatabase() {
        mSelectedMovieType = Constants.FAVORITE;
        // if the data is loaded before . restart the adapter to keep the data refreshed
        if (isLoadedBefore)
            restartLoader();
        else
            // first time loading data
            initLoader();
    }

    private void setupRecyclerView() {
        // create empty list
        mMoviesList = new ArrayList<>();
        //create adapter
        mMoviesAdapter = new MoviesAdapter(mMoviesList, this, this);
        // init layout manger
        // change number of movies in a single row according to phone orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // in case it's portrait
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            // in case it's landscape
            layoutManager = new GridLayoutManager(this, 4);
        }
        // set layout manger
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        // set movies adapter
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

    }

    private void getMoviesListFromServer() {
        if (InternetConnection.isNetworkAvailable(this)) {
            loadingIndicatorView.smoothToShow();
            // get presenter reference
            MoviesPresenter moviesPresenter = new MoviesPresenter(this);
            // make service call
            moviesPresenter.getMovies(mSelectedMovieType);

        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public void setMovies(List<Movie> moviesList) {
        this.mMoviesList = moviesList;
        updateAdapter();
    }

    @Override
    public void onFailure() {
        // hide loading dialog
        loadingIndicatorView.smoothToHide();
        // alert user with fail message
        Toast.makeText(this, R.string.movies_failure, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // save selected movie type
        outState.putInt(MOVIES_TYPE, mSelectedMovieType);
        // save recycler state
        mListState = layoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE, mListState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE);
    }

    @Override
    public void onMovieClick(Movie movie, ImageView imageView, TextView movieTitle) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        // init pairs
        Pair<View, String> posterPair = Pair.create((View) imageView, getString(R.string.poster));
        Pair<View, String> titlePair = Pair.create((View) movieTitle, getString(R.string.title));

        ActivityOptionsCompat imageTransitionOptions = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, posterPair, titlePair);


        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE, movie);
        intent.putExtras(bundle);
        startActivity(intent, imageTransitionOptions.toBundle());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MoviesAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {
        // clear old movies list
        mMoviesList.clear();
        // iterate through the cursor and fill moviesList with data
        for (int i = 0; i < mCursor.getCount(); i++) {
            // get data position in cursor
            int titleIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
            int imageIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER);
            int idIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID);
            int overViewIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            int averageRateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            int releaseDateIndex = mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            // get to the right location in the cursor
            mCursor.moveToPosition(i);
            // create new movie object
            Movie movie = new Movie();
            movie.setTitle(mCursor.getString(titleIndex));
            movie.setId(mCursor.getInt(idIndex));
            movie.setPosterPath(mCursor.getString(imageIndex));
            movie.setOverview(mCursor.getString(overViewIndex));
            movie.setVoteAverage(mCursor.getDouble(averageRateIndex));
            movie.setReleaseDate(mCursor.getString(releaseDateIndex));
            // add movie to list
            mMoviesList.add(movie);
        }
        // update the adapter
        updateAdapter();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    private void updateAdapter() {
        // hide loading dialog
        loadingIndicatorView.smoothToHide();
        // update adapter with the new data
        mMoviesAdapter.updateMoviesAdapter(mMoviesList);
        // restore list state ( position )
        if (mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }
}
