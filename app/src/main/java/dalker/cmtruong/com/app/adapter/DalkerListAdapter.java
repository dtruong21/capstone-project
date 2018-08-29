package dalker.cmtruong.com.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 27th
 */
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
        FirebaseStorage mStorage;
        StorageReference mRef;

        public DalkerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            mStorage = FirebaseStorage.getInstance();
            mRef = mStorage.getReference();
        }

        void bind(User user) {
            if (user.getPictureURL() != null) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(user.getPictureURL().getLarge());
                GlideApp.with(itemView.getContext())
                        .load(storageReference)
                        .error(R.mipmap.ic_launcher)
                        .into(mImageView);
            } else {
                GlideApp.with(itemView.getContext())
                        .asBitmap()
                        .load(R.mipmap.ic_launcher_foreground)
                        .into(mImageView);
            }

            if (user.getName() != null) {
                mDalkerName.setText(format("%s %s", user.getName().getFirstName(), user.getName().getLastName()));
                mDalkerAge.setText(format("Age: %s", String.valueOf(user.getDob().getAge())));
            } else {

            }
            mDalkerService.setText(format("%d doggo possible", user.getDogNumber()));
            List<Review> reviews;
            int rateSum = 0;
            float rateAverage;
            if (user.getReviews() != null && user.getReviews().size() > 0) {
                reviews = user.getReviews();
                for (int i = 0; i < reviews.size(); i++) {
                    rateSum += reviews.get(i).getRate();
                }
                rateAverage = rateSum / reviews.size();
                mDalkerRate.setText(String.format("%s/5", String.valueOf(rateAverage)));
            } else {
                mDalkerRate.setText(String.format("0/5"));
            }
            mDalkerPrice.setText(format("$%s/h", String.valueOf(user.getPrice())));
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClicked(v, getAdapterPosition());
        }
    }
}
