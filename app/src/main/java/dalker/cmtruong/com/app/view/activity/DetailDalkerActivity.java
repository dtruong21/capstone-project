package dalker.cmtruong.com.app.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.view.fragment.FragmentDetailDalker;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * Dalker detail activity
 *
 * @author davidetruong
 * @version 1.0
 * @since July 30th, 2018
 */
public class DetailDalkerActivity extends AppCompatActivity {

    private static final String TAG = DetailDalkerActivity.class.getSimpleName();
    @BindView(R.id.navigation_detail)
    BottomNavigationView navigation;

    @BindView(R.id.detail_dalker_container)
    FrameLayout mContainer;
    ArrayList<User> users;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dalker);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getDataAndTransferToFragment();
    }

    private void getDataAndTransferToFragment() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        users = bundle.getParcelableArrayList(getString(R.string.dalker_intent_detail));
        position = bundle.getInt(getString(R.string.dalker_intent_position));
        getFragmentManager().beginTransaction()
                .add(R.id.detail_dalker_container, FragmentDetailDalker.getInstance(users, position))
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_comment:
                    Timber.d("Comment is created");

                    return true;
                case R.id.navigation_call:
                    Timber.d("Call is created");

                    return true;
                case R.id.navigation_share:
                    Timber.d("Share is created");

                    return true;
            }
            return false;
        }
    };


}
