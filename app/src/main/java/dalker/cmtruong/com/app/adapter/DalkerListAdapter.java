package dalker.cmtruong.com.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

public class DalkerListAdapter extends RecyclerView.Adapter<DalkerListAdapter.DalkerViewHolder> {

    private static final String TAG = DalkerListAdapter.class.getSimpleName();
    private List<User> users;

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
        Timber.d("The user item: %s", user.toString());
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class DalkerViewHolder extends RecyclerView.ViewHolder {

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

        @BindView(R.id.dalker_add_favorite)
        ImageView mFavoriteIv;

        public DalkerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(User user) {

            Picasso.get()
                    .load(user.getPictureURL().getLarge())
                    .error(R.drawable.ic_launcher_foreground)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mImageView);
            mDalkerName.setText(String.format("%s %s", user.getName().getFirstName(), user.getName().getLastName()));
            mDalkerAge.setText(String.valueOf(user.getDob().getAge()));
            mDalkerService.setText(R.string.fake_service_user);
            mDalkerRate.setText(R.string.rate_fake_user);
            mDalkerPrice.setText(R.string.dalker_price_test);
        }
    }
}
