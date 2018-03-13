package com.mohnage7.popularmovies.network;


import com.mohnage7.popularmovies.model.MoviesResponse;
import com.mohnage7.popularmovies.model.ReviewsResponse;
import com.mohnage7.popularmovies.model.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("popular")
    Call<MoviesResponse> getPopularMovies();

    @GET("top_rated")
    Call<MoviesResponse> getTopRatedMovies();

    @GET("{id}/videos")
    Call<TrailersResponse> getTrailers(@Path("id") int id);

    @GET("{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int id);
}
