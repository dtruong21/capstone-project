package dalker.cmtruong.com.app.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.view.fragment.LoginFragment;
import timber.log.Timber;

/**
 * Login activity for dalker app which requires the connection with account and password
 *
 * @author davidetruong
 * @version 1.0
 * @since JUly 25th, 2018
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.profile_container)
    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Timber.tag(TAG);
        Timber.d("Open login activity");
        if (savedInstanceState == null) {
            addFragment();
        }

    }

    private void addFragment() {
        Intent intent = getIntent();
        String text = intent.getStringExtra(getString(R.string.intent_login));
        if (text.equals(getString(R.string.intent_user_default)))
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profile_container, LoginFragment.getInstance())
                    .addToBackStack(null)
                    .commit();
        else
            Timber.d("checked");
    }
}
