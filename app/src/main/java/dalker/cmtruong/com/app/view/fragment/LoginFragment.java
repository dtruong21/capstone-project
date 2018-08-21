package dalker.cmtruong.com.app.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 20th
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.login_id_et)
    EditText login;

    @BindView(R.id.password_et)
    EditText password;

    @BindView(R.id.login_bt)
    Button loginBt;

    @BindView(R.id.signup_bt)
    Button signupBt;

    public LoginFragment() {
    }

    public static LoginFragment getInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("Open login fragment");
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        openSignUpForm();
        return view;
    }

    private void openSignUpForm() {
        signupBt.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.profile_container, SignUpFragment.getInstance())
                    .addToBackStack(null)
                    .commit();
        });
    }


}
