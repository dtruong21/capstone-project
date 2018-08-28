package dalker.cmtruong.com.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.Review;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 25th
 */
public class DalkerReviewAdapter extends RecyclerView.Adapter<DalkerReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public DalkerReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    @Override
    public int getItemCount() {
        if (reviews.size() == 0)
            return 0;
        return 5;
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.m_rating)
        RatingBar mRating;

        @BindView(R.id.m_review)
        TextView mText;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Review review) {
            mRating.setRating(review.getRate());
            mText.setText(review.getComment());
        }
    }
}
