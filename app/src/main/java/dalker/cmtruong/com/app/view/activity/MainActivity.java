package dalker.cmtruong.com.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.helper.PreferencesHelper;
import dalker.cmtruong.com.app.service.DalkerRequestService;
import dalker.cmtruong.com.app.view.fragment.ListDalkerFragment;
import dalker.cmtruong.com.app.view.fragment.ListFavoriteDalkerFragment;
import dalker.cmtruong.com.app.view.fragment.ProfileFragment;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 21th
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.main_fragment_container)
    FrameLayout fragmentContainer;
    private static final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_search:
                Timber.d("SearchActivity is created");
                addNewFragment();
                return true;
            case R.id.navigation_favorite:
                Timber.d("FavoriteActivity is created");
                openDalkerDetail();
                return true;
            case R.id.navigation_profile:
                Timber.d("ProfileActivity is created");
                addProfileFragment();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
        Timber.d("Create once");
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_search);
        }
        Intent intent = getIntent();
        if (intent != null && intent.getAction().equals("fragment")) {
            Timber.d("Intent: %s", intent.toString());
            int position = intent.getIntExtra("fragment", 0);
            if (position == 0)
                navigation.setSelectedItemId(R.id.navigation_search);
            if (position == R.id.navigation_profile)
                navigation.setSelectedItemId(R.id.navigation_profile);
        }

        //addNewFragent();
    }

    private void addNewFragment() {
        Timber.d("Add new fragment");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ListDalkerFragment.getInstance())
                .commit();


    }

    private void openDalkerDetail() {
        Timber.d("Open Dalker detail activity");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ListFavoriteDalkerFragment.getInstance())
                .commit();

    }

    private void addProfileFragment() {
        Timber.d("Open profile fragment");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ProfileFragment.getInstance())
                .commit();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
