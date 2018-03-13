package com.mohnage7.popularmovies.trailers.presenter;


import com.mohnage7.popularmovies.model.TrailersResponse;
import com.mohnage7.popularmovies.network.Api;
import com.mohnage7.popularmovies.network.ApiClient;
import com.mohnage7.popularmovies.trailers.TrailersContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohnage7 on 3/1/2018.
 */

public class TrailersPresenter implements TrailersContract.IReviewsPresenter {

    private TrailersContract.IReviewsView iReviewsView;

    public TrailersPresenter(TrailersContract.IReviewsView iReviewsView) {
        this.iReviewsView = iReviewsView;
    }

    @Override
    public void getTrailers(int movieId) {
        Api reviewsAPI = ApiClient.getClient();
        Call<TrailersResponse> call = reviewsAPI.getTrailers(movieId);
        call.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                TrailersResponse trailersResponse = response.body();
                if (trailersResponse != null)
                    iReviewsView.SetTrailers(trailersResponse.getTrailers());
                else
                    iReviewsView.onFailure();
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                iReviewsView.onFailure();
            }
        });
    }
}
