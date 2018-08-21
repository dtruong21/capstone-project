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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.Login;
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
        logIn();
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

    private void logIn() {
        loginBt.setOnClickListener(v -> {
            FirebaseFirestore fb = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setTimestampsInSnapshotsEnabled(true)
                    .build();
            fb.setFirestoreSettings(settings);
            Login loginUser = new Login(login.getText().toString(), password.getText().toString());
            Gson gson = new Gson();
            String loginJson = gson.toJson(loginUser);
            Timber.d(loginJson);
            fb.collection(getString(R.string.users))
                    .whereEqualTo("login.username", login.getText().toString())
                    .whereEqualTo("login.password", password.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Timber.d("Verify : " + document.getId() + " ====> " + document.getData());
                                }
                            } else {
                                Timber.d("check");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Timber.d("failed");
                        }
                    });


        });
    }

    private boolean isValidLogin(String id, String password) {
        return !id.isEmpty() && !password.isEmpty();
    }


}
