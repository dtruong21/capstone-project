package dalker.cmtruong.com.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dalker.cmtruong.com.app.R;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 22th
 */
public class EditProfileFragment extends Fragment {

    public EditProfileFragment() {
    }

    public static EditProfileFragment getInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        return view;
    }
}
