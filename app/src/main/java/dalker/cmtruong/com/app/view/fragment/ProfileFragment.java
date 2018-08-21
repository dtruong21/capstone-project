package dalker.cmtruong.com.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.helper.PreferencesHelper;
import dalker.cmtruong.com.app.view.activity.LoginActivity;
import timber.log.Timber;

/**
 * Profile Fragment
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    String userSession = "";

    @BindView(R.id.signin_cardview)
    CardView signinCard;

    @BindView(R.id.edit_profile_cardview)
    CardView editCard;

    @BindView(R.id.logout_cardview)
    CardView logoutCard;

    public ProfileFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
    }

    public static ProfileFragment getInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        userSession = PreferencesHelper.getUserSession(getActivity());
        Timber.d("Open profile fragment with user: %s", userSession);
        View view = inflater.inflate(R.layout.fragment_profile_dalker, container, false);
        ButterKnife.bind(this, view);
        populateUIForProfile(userSession);
        login();
        return view;
    }

    /**
     * Change UI if User Session exists
     *
     * @param userSession
     */
    private void populateUIForProfile(String userSession) {
        if (userSession.isEmpty()) {
            signinCard.setVisibility(View.VISIBLE);
            editCard.setVisibility(View.GONE);
            logoutCard.setVisibility(View.GONE);
        } else {
            signinCard.setVisibility(View.GONE);
            editCard.setVisibility(View.VISIBLE);
            logoutCard.setVisibility(View.VISIBLE);
        }
    }

    private void login() {
        signinCard.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.putExtra(getString(R.string.intent_login), getString(R.string.intent_user_default));
            startActivity(intent);
        });
    }
}
