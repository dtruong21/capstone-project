package dalker.cmtruong.com.app.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.helper.PreferencesHelper;
import dalker.cmtruong.com.app.model.Login;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.view.activity.MainActivity;
import timber.log.Timber;

import static android.support.constraint.Constraints.TAG;

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

    @BindView(R.id.login_pb)
    ProgressBar mProgress;

    String data;
    String loginText;
    String passwordText;

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
        setRetainInstance(true);
        ButterKnife.bind(this, view);

        openSignUpForm();
        showData();
        logIn();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {

        } else {
            login.setText(savedInstanceState.getString(getString(R.string.login_state)));
            password.setText(savedInstanceState.getString(getString(R.string.password_state)));
        }
    }

    private void openSignUpForm() {
        signupBt.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.profile_container, SignUpFragment.getInstance())
                .addToBackStack(null)
                .commit());
    }

    private void logIn() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        loginBt.setOnClickListener(v -> {
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            Login loginUser = null;
            if (isValidLogin(login.getText().toString(), password.getText().toString())) {
                loginUser = new Login(login.getText().toString(), password.getText().toString());
            }
            Gson gson = new Gson();
            String loginJson = gson.toJson(loginUser);
            Timber.d(loginJson);
            loadingData();
            fb.collection(getString(R.string.users))
                    .whereEqualTo(getString(R.string.login_username), login.getText().toString())
                    .whereEqualTo("login.password", password.getText().toString())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timber.d("Verify : " + document.getId() + " ====> " + document.getData());
                                data = document.getData().toString();
                                PreferencesHelper.saveUserSession(getContext(), data);
                                PreferencesHelper.saveDocumentReference(getContext(), document.getId());
                            }
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("fragment", R.id.navigation_profile);
                            startActivity(intent);
                            Snackbar.make(getView(),
                                    "Error in login with user " + login.getText().toString(), Snackbar.LENGTH_SHORT).show();
                        } else {
                            Timber.d("check");
                            Snackbar.make(getView(),
                                    "Error in login with user " + login.getText().toString(), Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Timber.d("failed"));

        });
    }

    private boolean isValidLogin(String id, String password) {
        return !id.isEmpty() && !password.isEmpty();
    }

    private void loadingData() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void showData() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        loginText = login.getText().toString();
        passwordText = password.getText().toString();
        if (outState != null) {
            outState.putString(getString(R.string.login_state), loginText);
            outState.putString(getString(R.string.password_state), passwordText);
        }

    }
}
