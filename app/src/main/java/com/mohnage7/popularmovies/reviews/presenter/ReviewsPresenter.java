package com.mohnage7.popularmovies.reviews.presenter;


import com.mohnage7.popularmovies.model.ReviewsResponse;
import com.mohnage7.popularmovies.network.Api;
import com.mohnage7.popularmovies.network.ApiClient;
import com.mohnage7.popularmovies.reviews.ReviewContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohnage7 on 3/1/2018.
 */

public class ReviewsPresenter implements ReviewContract.IReviewsPresenter {

    private ReviewContract.IReviewsView iReviewsView;

    public ReviewsPresenter(ReviewContract.IReviewsView iReviewsView) {
        this.iReviewsView = iReviewsView;
    }

    @Override
    public void getReviews(int movieId) {
        Api reviewsAPI = ApiClient.getClient();
        Call<ReviewsResponse> call = reviewsAPI.getReviews(movieId);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                ReviewsResponse moviesResponse = response.body();
                if (moviesResponse != null)
                    iReviewsView.setReviews(moviesResponse.getReviews());
                else
                    iReviewsView.onFailure();
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                iReviewsView.onFailure();
            }
        });
    }
}
