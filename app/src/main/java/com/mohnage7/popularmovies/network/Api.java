package com.mohnage7.popularmovies.network;


import com.mohnage7.popularmovies.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    @GET("popular")
    Call<MoviesResponse> getPopularMovies();

    @GET("top_rated")
    Call<MoviesResponse> getTopRatedMovies();
}
