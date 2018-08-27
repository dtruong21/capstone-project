package dalker.cmtruong.com.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.event.OnDalkerHandledListener;
import dalker.cmtruong.com.app.helper.GlideApp;
import dalker.cmtruong.com.app.model.Review;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

import static java.lang.String.format;

public class DalkerListAdapter extends RecyclerView.Adapter<DalkerListAdapter.DalkerViewHolder> {

    private static final String TAG = DalkerListAdapter.class.getSimpleName();
    private List<User> users;
    private OnDalkerHandledListener mListener;

    public DalkerListAdapter(List<User> users) {
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
        this.users = users;
    }

    @NonNull
    @Override
    public DalkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.dalker_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new DalkerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DalkerViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user);
    }

    public void setOnItemClickedListener(OnDalkerHandledListener mListener) {
        this.mListener = mListener;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class DalkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.dalker_iv)
        ImageView mImageView;

        @BindView(R.id.dalker_name_tv)
        TextView mDalkerName;

        @BindView(R.id.dalker_age_tv)
        TextView mDalkerAge;

        @BindView(R.id.dalker_service_tv)
        TextView mDalkerService;

        @BindView(R.id.dalker_rate_tv)
        TextView mDalkerRate;

        @BindView(R.id.dalker_price_tv)
        TextView mDalkerPrice;

        public DalkerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(User user) {

            GlideApp.with(itemView)
                    .load(user.getPictureURL().getLarge())
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mImageView);
            mDalkerName.setText(format("%s %s", user.getName().getFirstName(), user.getName().getLastName()));
            mDalkerAge.setText(format("Age: %s", String.valueOf(user.getDob().getAge())));
            mDalkerService.setText(format("%d doggo possible", user.getDogNumber()));
            ArrayList<Review> reviews = user.getReviews();
            int rateSum = 0;
            float rateAverage = 0.0f;
            if (reviews.size() > 0) {
                for (Review review : reviews) rateSum += review.getRate();
                rateAverage = rateSum / reviews.size();
            }
            mDalkerRate.setText(String.format("%s/5", String.valueOf(rateAverage)));
            mDalkerPrice.setText(format("%s per hour", String.valueOf(user.getPrice())));
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(v, getAdapterPosition());
        }
    }
}
