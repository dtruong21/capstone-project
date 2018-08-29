package dalker.cmtruong.com.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.helper.PreferencesHelper;
import dalker.cmtruong.com.app.model.Review;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 17th
 */
public class CommentFragment extends DialogFragment {

    private static final String DALKER_KEY = "DALKER_KEY";
    @BindView(R.id.comment_rb)
    RatingBar mRatingBar;

    @BindView(R.id.comment_et)
    EditText editText;

    int rate;
    String review;
    String id;
    User user;

    FirebaseFirestore fb;
    DatabaseReference mDB;

    public CommentFragment() {
    }

    public static CommentFragment getInstance(User dalker) {
        CommentFragment commentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(DALKER_KEY, dalker);
        commentFragment.setArguments(bundle);
        return commentFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_comment, null);
        ButterKnife.bind(this, view);
        fb = FirebaseFirestore.getInstance();
        mDB = FirebaseDatabase.getInstance().getReference(getString(R.string.users));
        if (getArguments() != null) {
            Timber.d("Arguments: %s", getArguments().toString());
            user = getArguments().getParcelable(DALKER_KEY);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if (savedInstanceState == null) {
            builder.setView(view);
            builder.setTitle(getString(R.string.comment));
            mRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
                Timber.d("Rating: %s", ratingBar.getRating());
                rate = (int) ratingBar.getRating();
            });

            id = "_" + getIDReview();
            builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(getString(R.string.done), (dialog, which) -> {
                        review = editText.getText().toString();
                        Review reviewContent = new Review(id, rate, review);
                        ArrayList<Review> reviews;
                        if (user.getReviews() != null) {
                            reviews = user.getReviews();
                        } else {
                            reviews = new ArrayList<>();
                        }
                        reviews.add(reviewContent);
                        Map<String, ArrayList<Review>> data = new HashMap<>();
                        data.put(getString(R.string.reviews_filed), reviews);
                        Gson gson = new Gson();
                        String json = gson.toJson(reviewContent);
                        Map<String, Object> reviewFF = new Gson().fromJson(
                                json, new TypeToken<HashMap<String, Object>>() {
                                }.getType()
                        );
                        fb.collection(getString(R.string.users)).document(user.getIdUser())
                                .update(getString(R.string.reviews_filed), FieldValue.arrayUnion(reviewFF));

                        mDB.child(user.getIdUser()).child(getString(R.string.m_reviews))
                                .setValue(reviews)
                                .addOnCompleteListener(task -> Timber.d("Yeah"))
                                .addOnFailureListener(e -> Timber.d("Error adding comment"));

                    });
        } else {
            Timber.d("State changed");
        }

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private String getIDReview() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

}