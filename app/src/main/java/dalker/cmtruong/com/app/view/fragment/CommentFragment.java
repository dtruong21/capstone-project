package dalker.cmtruong.com.app.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
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

    Float rate;
    String review;
    String id;

    User user;

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
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.fragment_comment, null);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            Timber.d("Arguments: %s", getArguments().toString());
            user = getArguments().getParcelable(DALKER_KEY);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle(getString(R.string.comment));
        mRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            Timber.d("Rating: %s", ratingBar.getRating());
            rate = ratingBar.getRating();
        });
        review = editText.getText().toString();
        id = "_" + getIDReview();
        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder.create();
    }

    private String getIDReview() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
