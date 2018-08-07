package dalker.cmtruong.com.app.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.BuildConfig;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.database.AppExecutors;
import dalker.cmtruong.com.app.database.DalkerDatabase;
import dalker.cmtruong.com.app.model.Review;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.viewmodel.AddFavoriteDalkerVMFactory;
import dalker.cmtruong.com.app.viewmodel.AddFavoriteDalkerViewModel;
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
    TextView dalkerDescription;

    @BindView(R.id.dalker_review_list)
    RecyclerView reviewRV;

    ArrayList<Review> reviews;
    double rateAverage;

    @BindView(R.id.mToolbar)
    Toolbar mToolbar;

    @BindView(R.id.share_fab)
    FloatingActionButton fab;

    @BindView(R.id.comment_bt)
    Button comment_bt;

    @BindView(R.id.call_bt)
    Button call_bt;

    @BindView(R.id.insert_to_fav)
    ImageView insert_bt;

    private DalkerDatabase mDB;

    private static final int DEFAULT_TASK_ID = -1;

    private int mUserId = DEFAULT_TASK_ID;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_dalker, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        mDB = DalkerDatabase.getsInstance(getActivity().getApplicationContext());
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getArguments() != null) {
            users = getArguments().getParcelableArrayList(USER_LIST);
            position = getArguments().getInt(USER_POSITION);
        }
        initData(users.get(position));
        return view;
    }

    /**
     * Function to init the fragment
     *
     * @param user
     */
    private void initData(final User user) {
        Picasso.get()
                .load(user.getPictureURL().getLarge())
                .error(R.mipmap.ic_launcher_foreground)
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(mDalkerPhoto);
        dalkerNameTv.setText(String.format("%s %s", user.getName().getFirstName(), user.getName().getLastName()));
        dalkerAddressTv.setText(String.format("%s, %s", user.getLocation().getStreet(), user.getLocation().getCity()));
        dalkerPriceTv.setText(String.format("Price: %s", getString(R.string.dalker_price_test)));

        if (user.getDescription() == null)
            if (BuildConfig.FLAVOR.equals(getString(R.string.demo)))
                dalkerDescription.setText(getString(R.string.text_test));
            else
                dalkerDescription.setText(R.string.description_empty_error);
        else
            dalkerDescription.setText(user.getDescription());
        reviews = user.getReviews();
        rateAverage = getRateAverage(reviews);
        if (user.getReviews() == null && BuildConfig.FLAVOR.equals(getString(R.string.demo)))
            dalkerRatingBar.setRating(4.5f);
        else
            dalkerRatingBar.setRating((float) rateAverage);
        setupButton(user);
        setupFavorite();
        Timber.d("Call the event here");
        insert_bt.setOnClickListener(v -> {
            AppExecutors.getInstance().diskIO().execute(() -> {
                if (mUserId == DEFAULT_TASK_ID) {
                    Timber.d("Something wrong here");
                    mDB.userDAO().insertUser(user);
                    String text = "Add " + user.getName().getFirstName() + " " + user.getName().getLastName() + " with successfull to favorite list";
                    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
                    Timber.d("user:" + user.toString());
                    insert_bt.setImageResource(R.drawable.ic_favorite_black_48dp);
                } else {
                    Timber.d("Something wrong here too");
                    Timber.d("user ID: " + String.valueOf(mUserId));
                    user.setIdUser(mUserId);
                    mDB.userDAO().updateUser(user);
                    String text = "Update " + user.getName().getFirstName() + " " + user.getName().getLastName() + " with successfull to favorite list";
                    Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
                    Timber.d(user.toString());
                }
            });
        });
    }

    /**
     * Function to calcul dalker average rate
     *
     * @param reviews
     * @return average|sum
     */
    private double getRateAverage(ArrayList<Review> reviews) {
        int sum = 0;
        double average;
        if (reviews != null) {
            for (Review review : reviews)
                sum += review.getRate();
            average = sum / reviews.size();
            return average;
        }
        return sum;
    }

    /**
     * function to setup the FAB button
     *
     * @param user
     */
    private void setupButton(final User user) {
        fab.setOnClickListener(v -> {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            share.putExtra(Intent.EXTRA_TEXT, user.getName().getFirstName() + " " + user.getName().getLastName());
            startActivity(Intent.createChooser(share, getString(R.string.share)));
        });

        call_bt.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(getString(R.string.tel), 0 + user.getPhone(), null));
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d(String.valueOf(item.getItemId()));
        return super.onOptionsItemSelected(item);
    }

    private void setupFavorite() {
        if (mUserId == DEFAULT_TASK_ID) {
            AddFavoriteDalkerVMFactory factory = new AddFavoriteDalkerVMFactory(mDB, mUserId);
            final AddFavoriteDalkerViewModel viewModel = ViewModelProviders.of(this, factory).get(AddFavoriteDalkerViewModel.class);
            viewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    viewModel.getUser().removeObserver(this);
                    if (user == null)
                        return;
                    insert_bt.setImageResource(R.drawable.ic_favorite_black_48dp);
                    Timber.d("check 1");
                }
            });
        }

    }

}
