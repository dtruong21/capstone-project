package dalker.cmtruong.com.app.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;


/**
 * @author davidetruong
 * @version 1.0
 * @since 03 August 2018
 */
public class FragmentDetailDalker extends Fragment {

    private static final String TAG = FragmentDetailDalker.class.getSimpleName();
    View view;

    private static final String USER_LIST = "USER_LIST";
    private static final String USER_POSITION = "USER_POSITION";
    ArrayList<User> users;
    int position;

    @BindView(R.id.dalker_detail_photo)
    CircleImageView mDalkerPhoto;

    @BindView(R.id.dalker_detail_name_tv)
    TextView dalkerNameTv;

    @BindView(R.id.dalker_detail_address_tv)
    TextView dalkerAddressTv;

    @BindView(R.id.dalker_detail_tarif_tv)
    TextView dalkerPriceTv;

    @BindView(R.id.dalker_rating_bar)
    RatingBar dalkerRatingBar;

    @BindView(R.id.dalker_detail_text)
    TextView dalkerDescrition;

    @BindView(R.id.dalker_review_list)
    RecyclerView reviewRV;

    public FragmentDetailDalker() {
    }

    public static FragmentDetailDalker getInstance(ArrayList<User> users, int position) {
        FragmentDetailDalker mFragment = new FragmentDetailDalker();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(USER_LIST, users);
        bundle.putInt(USER_POSITION, position);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_dalker, container, false);
        ButterKnife.bind(this, view);
        Timber.d("fragment detail is created");
        if (getArguments() != null) {
            Timber.d("Let's roll ...");
            users = getArguments().getParcelableArrayList(USER_LIST);
            Timber.d("Resultats: %s", users.toString());
            position = getArguments().getInt(USER_POSITION);
            Timber.d("My position: " + position);
        }
        initData(users.get(position));
        return view;
    }

    private void initData(User user) {
        Picasso.get()
                .load(user.getPictureURL().getLarge())
                .error(R.mipmap.ic_launcher_foreground)
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(mDalkerPhoto);
        dalkerNameTv.setText(String.format("%s %s", user.getName().getFirstName(), user.getName().getLastName()));
        dalkerAddressTv.setText(String.format("%s, %s", user.getLocation().getStreet(), user.getLocation().getCity()));
        dalkerPriceTv.setText(String.format("Price: %s", getString(R.string.dalker_price_test)));
        dalkerDescrition.setText(getString(R.string.text_test));
    }
}
