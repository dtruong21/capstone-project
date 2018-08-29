package dalker.cmtruong.com.app.view.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.Login;
import dalker.cmtruong.com.app.model.Review;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.view.activity.LoginActivity;
import timber.log.Timber;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

/**
 * TODO: Sign up form
 *
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 21th
 */
public class SignUpFragment extends Fragment {

    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "login";
    private static final Random random = new Random();
    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
    private static final int TOKEN_LENGTH = 16;


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

    @BindView(R.id.signup_pb)
    ProgressBar mProgressBar;

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
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        Timber.d("Sign up fragment");
        hide();
        signIn();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

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
        return !confirm.isEmpty() && !password.isEmpty() && confirm.equals(password);
    }

    private boolean isValidForm(String email, String id, String password, String passwordConfirm) {
        return !email.isEmpty() && !id.isEmpty() && isConfirmed(password, passwordConfirm);
    }

    void hide() {
        mProgressBar.setVisibility(View.GONE);
    }

    void show() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void signIn() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        createAccount.setOnClickListener((View v) -> {
            show();
            if (isValidForm(email.getText().toString(), loginId.getText().toString(), password.getText().toString(), confirmPassword.getText().toString())) {
                User user = new User();
                user.setEmail(email.getText().toString());
                Login login = new Login(loginId.getText().toString(), password.getText().toString());
                String id = getToken(TOKEN_LENGTH) + "_" + loginId.getText().toString();
                user.setIdUser(id);
                ArrayList<Review> reviews = new ArrayList<>();
                user.setReviews(reviews);
                login.setId(id);
                user.setLogin(login);
                //FirebaseFirestore db = FirebaseFirestore.getInstance();
                //String json = convertToJson(user);
//                Map<String, Object> userFF = new Gson().fromJson(
//                        json, new TypeToken<HashMap<String, Object>>() {
//                        }.getType()
//                );
//                db.collection(getString(R.string.users))
//                        .document(id)
//                        .set(userFF)
//                        .addOnSuccessListener(documentReference -> {
//                            Intent notificationIntent = new Intent(getContext(), LoginActivity.class);
//                            notificationIntent.putExtra(getString(R.string.intent_login), getString(R.string.intent_user_default));
//                            PendingIntent contentIntent = PendingIntent.getActivity(v.getContext(), 0, notificationIntent, 0);
//                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(v.getContext())
//                                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
//                                    .setContentTitle(getString(R.string.app_name))
//                                    .setContentText(getString(R.string.add_user) + loginId.getText().toString() + getString(R.string.with_success))
//                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.add_user) + loginId.getText().toString() + getString(R.string.with_success)))
//                                    .setPriority(NotificationCompat.PRIORITY_MAX);
//                            mBuilder.setContentIntent(contentIntent);
//
//                            NotificationManager notificationManager =
//                                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), IMPORTANCE_HIGH);
//                                notificationManager.createNotificationChannel(mChannel);
//                            }
//                            notificationManager.notify(NOTIFY_ID, mBuilder.build());
//                            Timber.d("add new user%s", id);
//
//                            getActivity().getSupportFragmentManager().beginTransaction()
//                                    .replace(R.id.profile_container, LoginFragment.getInstance())
//                                    .addToBackStack(null)
//                                    .commit();
//                            Snackbar.make(getView(), getString(R.string.add_user) + loginId.getText().toString() + getString(R.string.with_success), Toast.LENGTH_LONG).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            Timber.d("add failed");
//                            Snackbar.make(getView(), getString(R.string.failed_add) + loginId.getText().toString() + getString(R.string.to_the_database), Toast.LENGTH_LONG).show();
//                        });
                DatabaseReference mDB = FirebaseDatabase.getInstance().getReference();
                mDB.child(getString(R.string.users)).child(user.getIdUser()).setValue(user)
                        .addOnCompleteListener(task -> {
                            Intent notificationIntent = new Intent(getContext(), LoginActivity.class);
                            notificationIntent.putExtra(getString(R.string.intent_login), getString(R.string.intent_user_default));
                            PendingIntent contentIntent = PendingIntent.getActivity(v.getContext(), 0, notificationIntent, 0);
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(v.getContext())
                                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                                    .setContentTitle(getString(R.string.app_name))
                                    .setContentText(getString(R.string.add_user) + loginId.getText().toString() + getString(R.string.with_success))
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.add_user) + loginId.getText().toString() + getString(R.string.with_success)))
                                    .setPriority(NotificationCompat.PRIORITY_MAX);
                            mBuilder.setContentIntent(contentIntent);

                            NotificationManager notificationManager =
                                    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), IMPORTANCE_HIGH);
                                notificationManager.createNotificationChannel(mChannel);
                            }
                            notificationManager.notify(NOTIFY_ID, mBuilder.build());
                            Timber.d("add new user%s", id);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.profile_container, LoginFragment.getInstance())
                                    .addToBackStack(null)
                                    .commit();
                            Snackbar.make(getView(), getString(R.string.add_user) + loginId.getText().toString() + getString(R.string.with_success), Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {

                            Timber.d("add failed");
                            Snackbar.make(getView(), getString(R.string.failed_add) + loginId.getText().toString() + getString(R.string.to_the_database), Toast.LENGTH_LONG).show();
                        });

                hide();
            } else {
                handleError();
            }
        });

    }

    private String convertToJson(User user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public static String getToken(int length) {
        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putString(getString(R.string.email_key), email.getText().toString());
            outState.putString(getString(R.string.login_key), loginId.getText().toString());
            outState.putString(getString(R.string.pasword_key), password.getText().toString());
            outState.putString(getString(R.string.confirm_key), confirmPassword.getText().toString());
        }
    }
}
