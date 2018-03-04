package com.mohnage7.popularmovies.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohnage7.popularmovies.MoviesContract;
import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Movie;
import com.mohnage7.popularmovies.presenter.MoviesPresenter;
import com.mohnage7.popularmovies.utils.InternetConnection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.mohnage7.popularmovies.utils.Constants.MOVIE;

public class MoviesActivity extends AppCompatActivity implements MoviesContract.IMoviesView, MoviesAdapter.OnMovieClickListener {

    @BindView(R.id.movies_recycler_view)
    RecyclerView mMoviesRecyclerView;

    private List<Movie> mMoviesList;
    private MoviesAdapter mMoviesAdapter;
    private int mSelectedMovieType = 0;
    private static final String MOVIES_TYPE = "movies_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        // get movies type if exists
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_TYPE))
            mSelectedMovieType = savedInstanceState.getInt(MOVIES_TYPE);

        // bind views
        ButterKnife.bind(this);
        // getting recycler ready
        setupRecyclerView();
        // get movies data from the server
        getMoviesListFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter_menu)
            showFilterDialog();
        return true;
    }


    void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.filter_movies);
        //list of items
        builder.setSingleChoiceItems(getMoviesTypeArray(), mSelectedMovieType,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        mSelectedMovieType = which;
                        getMoviesListFromServer();
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public String[] getMoviesTypeArray() {
        return new String[]{getString(R.string.popular_movies), getString(R.string.top_rated_movies)};
    }

    private void setupRecyclerView() {
        // create empty list
        mMoviesList = new ArrayList<>();
        //create adapter
        mMoviesAdapter = new MoviesAdapter(mMoviesList, this, this);
        // create layout manger
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        // set layout manger
        mMoviesRecyclerView.setLayoutManager(layoutManager);
        // set movies adapter
        mMoviesRecyclerView.setAdapter(mMoviesAdapter);

    }

    private void getMoviesListFromServer() {
        if (InternetConnection.isNetworkAvailable(this)) {
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
        mMoviesAdapter.updateMoviesAdapter(mMoviesList);
    }

    @Override
    public void onFailure() {
        Toast.makeText(this, R.string.movies_failure, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MOVIES_TYPE, mSelectedMovieType);
        super.onSaveInstanceState(outState);
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
}
