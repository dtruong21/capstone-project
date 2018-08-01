package dalker.cmtruong.com.app.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.helper.PreferencesHelper;
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
        Timber.d("Open profile fragment");
        if (userSession.isEmpty())
            return inflater.inflate(R.layout.fragment_profile_guest, container, false);
        else
            return inflater.inflate(R.layout.fragment_profile_dalker, container, false);
    }
}
