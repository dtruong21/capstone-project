package dalker.cmtruong.com.app.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

public class ListDalkerFragment extends Fragment {
    private static final String TAG = ListDalkerFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
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
        Timber.d("Fragment Demo ListDalker is created");
        return view;
    }
}
