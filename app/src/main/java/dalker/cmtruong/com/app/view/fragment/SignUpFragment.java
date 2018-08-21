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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import timber.log.Timber;

/**
 * TODO: Sign up form
 *
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 21th
 */
public class SignUpFragment extends Fragment {

    @BindView(R.id.email_add_et)
    EditText email;

    @BindView(R.id.id_add_et)
    EditText loginId;

    @BindView(R.id.pass_add_et)
    EditText password;

    @BindView(R.id.pass_confirm_et)
    EditText confirmPassword;

    @BindView(R.id.create_account_bt)
    Button createAccount;

    @BindView(R.id.password_error)
    TextView passwordError;

    @BindView(R.id.confirm_error)
    TextView confirmError;

    public SignUpFragment() {
    }

    public static SignUpFragment getInstance() {
        SignUpFragment fragment = new SignUpFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in_form, container, false);
        ButterKnife.bind(this, view);
        handleError();
        return view;
    }

    private void handleError() {
        if (isValidPassword(password.getText().toString()))
            passwordError.setVisibility(View.VISIBLE);
        if (isConfirmed(password.getText().toString(), confirmPassword.getText().toString()))
            confirmError.setVisibility(View.VISIBLE);
    }

    private static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isConfirmed(String password, String confirm) {
        if (password.equals(confirm))
            return true;
        else
            return false;
    }



}
