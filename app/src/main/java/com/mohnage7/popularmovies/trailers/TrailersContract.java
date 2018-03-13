package com.mohnage7.popularmovies.trailers;

import com.mohnage7.popularmovies.model.Trailers;

import java.util.List;

/**
 * Created by mohnage7 on 3/1/2018.
 * Interface that defines the interaction between Presenter and the view
 */

public class TrailersContract {

    public interface IReviewsView {
        void SetTrailers(List<Trailers> trailersList);

        void onFailure();
    }

    public interface IReviewsPresenter {
        void getTrailers(int movieId);
    }
}
