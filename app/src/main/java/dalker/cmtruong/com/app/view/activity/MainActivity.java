package dalker.cmtruong.com.app.view.activity;

import android.content.Intent;
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
import timber.log.Timber;

/**
 * Main activity for dalker app which displays the list of dalker available
 *
 * @author davidetruong
 * @version 1.0
 * @since JUly 25th, 2018
 */
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
                    onResume();
                    return true;
                case R.id.navigation_favorite:
                    Timber.d("FavoriteActivity is created");
                    openDalkerDetail();
                    return true;
                case R.id.navigation_profile:
                    Timber.d("ProfileActivity is created");
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
        addNewFragment();
    }

    private void addNewFragment() {
        if (fragmentContainer != null) {
            Timber.d("Add new fragment");
            getFragmentManager().beginTransaction()
                    .add(R.id.main_fragment_container, ListDalkerFragment.getInstance())
                    .commit();
        } else {
            Timber.d("Add new fragment");
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, ListDalkerFragment.getInstance())
                    .addToBackStack(null)
                    .commit();
        }

    }

    private void openDalkerDetail() {
        Timber.d("Open Dalker detail activity");
        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, ListFavoriteDalkerFragment.getInstance())
                .addToBackStack(null)
                .commit();
    }


}
