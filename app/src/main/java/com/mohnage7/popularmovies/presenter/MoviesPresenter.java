package com.mohnage7.popularmovies.presenter;


import com.mohnage7.popularmovies.MoviesContract;
import com.mohnage7.popularmovies.model.MoviesResponse;
import com.mohnage7.popularmovies.network.Api;
import com.mohnage7.popularmovies.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohnage7 on 3/1/2018.
 */

public class MoviesPresenter implements MoviesContract.IMoviesPresenter {

    private MoviesContract.IMoviesView iMoviesView;

    public MoviesPresenter(MoviesContract.IMoviesView iMoviesView) {
        this.iMoviesView = iMoviesView;
    }

    @Override
    public void getMovies(int moviesType) {
        Api popularMoviesAPi = ApiClient.getClient();
        Call<MoviesResponse> call;

        if (moviesType == 0)
            call = popularMoviesAPi.getPopularMovies();
        else
            call = popularMoviesAPi.getTopRatedMovies();

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                MoviesResponse moviesResponse = response.body();
                if (moviesResponse != null)
                    iMoviesView.setMovies(moviesResponse.getMovieList());
                else
                    iMoviesView.onFailure();
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                iMoviesView.onFailure();
            }
        });

    }
}
