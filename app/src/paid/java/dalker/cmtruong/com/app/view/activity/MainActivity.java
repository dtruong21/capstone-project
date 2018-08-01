package dalker.cmtruong.com.app.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.view.fragment.ListDalkerFragment;
import dalker.cmtruong.com.app.view.fragment.ListFavoriteDalkerFragment;
import dalker.cmtruong.com.app.view.fragment.ProfileFragment;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.main_fragment_container)
    FrameLayout fragmentContainer;
    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    Timber.d("MainActivity is created");
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
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //addNewFragment();
        if (getFragmentManager().findFragmentById(R.id.main_fragment_container) == null)
            navigation.setSelectedItemId(R.id.navigation_search);

    }

    private void addNewFragment() {
        Timber.d("Add new fragment");
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ListDalkerFragment.getInstance())
                .addToBackStack(null)
                .commit();
    }

    private void openDalkerDetail() {
        Timber.d("Open Dalker detail activity");
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ListFavoriteDalkerFragment.getInstance())
                .addToBackStack(null)
                .commit();
    }

    private void addProfileFragment() {
        Timber.d("Open profile fragment");
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ProfileFragment.getInstance())
                .addToBackStack(null)
                .commit();
    }


}
