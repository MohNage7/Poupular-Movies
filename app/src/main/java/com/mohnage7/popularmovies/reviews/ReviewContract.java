package com.mohnage7.popularmovies.reviews;

import com.mohnage7.popularmovies.model.Review;

import java.util.List;

/**
 * Created by mohnage7 on 3/1/2018.
 * Interface that defines the interaction between Presenter and the view
 */

public class ReviewContract {

    public interface IReviewsView {
        void setReviews(List<Review> moviesList);

        void onFailure();
    }

    public interface IReviewsPresenter {
        void getReviews(int movieId);
    }
}
