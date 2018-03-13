package com.mohnage7.popularmovies.reviews.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mohnage7.popularmovies.R;
import com.mohnage7.popularmovies.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohnage7 on 2/28/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> mReviewsList;

    ReviewsAdapter(List<Review> reviewsList) {
        mReviewsList = reviewsList;
    }

    void updateReviewsAdapter(List<Review> reviewsList) {
        this.mReviewsList = reviewsList;
        notifyDataSetChanged();
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReviewsViewHolder holder, int position) {
        final Review review = mReviewsList.get(position);
        // set review name
        holder.reviewerNameTxtView.setText(review.getAuthor());
        // set review
        holder.reviewTxtView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewsList.size();
    }


    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reviewer_name_txt_view)
        TextView reviewerNameTxtView;
        @BindView(R.id.review_txt_view)
        TextView reviewTxtView;

        ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
