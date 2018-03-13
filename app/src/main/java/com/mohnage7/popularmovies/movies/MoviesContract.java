package com.mohnage7.popularmovies.movies;

import com.mohnage7.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by mohnage7 on 3/1/2018.
 * Interface that defines the interaction between Presenter and the view
 */

public class MoviesContract {

    public interface IMoviesView {
        void setMovies(List<Movie> moviesList);

        void onFailure();
    }

    public interface IMoviesPresenter {
        void getMovies(int moviesType);
    }
}
