package dalker.cmtruong.com.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.view.fragment.FragmentDetailDalker;
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
        getDataAndTransferToFragment();
    }

    private void getDataAndTransferToFragment() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        users = bundle.getParcelableArrayList(getString(R.string.dalker_intent_detail));
        position = bundle.getInt(getString(R.string.dalker_intent_position));
        getSupportFragmentManager().beginTransaction()
                .add(R.id.detail_dalker_container, FragmentDetailDalker.getInstance(users, position))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else
            super.onBackPressed();
    }
}
