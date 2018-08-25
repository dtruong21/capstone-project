package dalker.cmtruong.com.app.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.BuildConfig;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.adapter.DalkerListAdapter;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.view.activity.DetailDalkerActivity;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 1st August, 2018
 */
public class ListDalkerFragment extends Fragment {
    private static final String TAG = ListDalkerFragment.class.getSimpleName();
    @BindView(R.id.dalker_list_rv)
    RecyclerView mDalkerRV;

    @BindView(R.id.dalker_search_error)
    TextView mErrorMessage;

    DalkerListAdapter adapter;
    ArrayList<User> users = null;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.location_et)
    EditText locationET;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mLayout;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;

    protected Location mLastLocation;

    String mLatitudeLabel;
    String mLongitudeLabel;

    private RewardedVideoAd rewardedVideoAd;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ListDalkerFragment() {
    }

    public static ListDalkerFragment getInstance() {
        ListDalkerFragment fragment = new ListDalkerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_dalker, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        Timber.d("Fragment Free ListDalker is created");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(true);
        initData();
        mLayout.setOnRefreshListener(this::disableRefresh);
        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }

        locationET.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Timber.d("handle abc " + v.getText());
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            return false;
        });
        return view;
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (shouldProvideRationale) {
            Timber.d("Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    view -> {
                        // Request permission
                        startLocationPermissionRequest();
                    });

        } else {
            Timber.d("Requesting permission");
            startLocationPermissionRequest();
        }
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void disableRefresh() {
        mLayout.setRefreshing(false);
    }

    private void initData() {
        openAds();
        makeFakeUserQuery();
    }

    private void showDalkerList() {
        mDalkerRV.setVisibility(View.VISIBLE);
        mLayout.setRefreshing(false);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void showMessageError() {
        mDalkerRV.setVisibility(View.GONE);
        mLayout.setRefreshing(false);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void waitingForResult() {
        mDalkerRV.setVisibility(View.GONE);
        mLayout.setRefreshing(true);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void makeFakeUserQuery() {

    }

    private void showDetailDalker(DalkerListAdapter adapter) {
        adapter.setOnItemClickedListener((view, position) -> {
            Intent intent = new Intent(getActivity(), DetailDalkerActivity.class);
            Bundle b = new Bundle();
            b.putParcelableArrayList(getString(R.string.dalker_intent_detail), users);
            b.putInt(getString(R.string.dalker_intent_position), position);
            intent.putExtras(b);
            startActivity(intent);
        });
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(getView().findViewById(R.id.my_ll),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }

    private void showSnackbar(final String text) {
        View container = getView().findViewById(R.id.my_ll);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastLocation = task.getResult();
                        Timber.d("My location is: " + mLastLocation.getLatitude() + "_" + mLastLocation.getLongitude());
                        getAddressFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    } else {
                        showSnackbar(getString(R.string.no_location_detected));
                    }
                })
                .addOnFailureListener(e -> Timber.d("Failed to get location"));
    }

    private void getAddressFromLocation(double latitude, double longtitude) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
            Address fetchAddress = addresses.get(0);
            Timber.d("Current location: %s", fetchAddress.getSubAdminArea());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAds() {
        MobileAds.initialize(getContext(), getString(R.string.admob_app_id));
        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getContext());
        Timber.d("started ad");
        rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Timber.d("Ad loaded");
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Timber.d("Ad opened");
            }

            @Override
            public void onRewardedVideoStarted() {
                Timber.d("Ad started");
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Timber.d("Ad closed");
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                Timber.d("Ad item");

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Timber.d("Ad left application");
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Timber.d("Ad failed " + i);
            }

            @Override
            public void onRewardedVideoCompleted() {
                Timber.d("Add completed");
            }
        });

        rewardedVideoAd.loadAd(getString(R.string.ad_unit_id), new AdRequest.Builder().build());
    }

    @Override
    public void onResume() {
        rewardedVideoAd.resume(getContext());
        super.onResume();
    }

    @Override
    public void onPause() {
        rewardedVideoAd.pause(getContext());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        rewardedVideoAd.destroy(getContext());
        super.onDestroy();
    }
}
