package dalker.cmtruong.com.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import timber.log.Timber;

/**
 * Favorite Fragment
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
public class ListFavoriteDalkerFragment extends Fragment {

    private static final String TAG = ListFavoriteDalkerFragment.class.getSimpleName();

    public ListFavoriteDalkerFragment() {
    }

    public static ListFavoriteDalkerFragment getInstance() {
        ListFavoriteDalkerFragment fragment = new ListFavoriteDalkerFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        ButterKnife.bind(this, view);
        Timber.d("Open favorite fragment");
        return view;
    }
}
