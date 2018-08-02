package dalker.cmtruong.com.app.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * Dalker detail activity
 *
 * @author davidetruong
 * @version 1.0
 * @since July 30th, 2018
 */
public class DetailDalkerActivity extends AppCompatActivity {

    private static final String TAG = DetailDalkerActivity.class.getSimpleName();

    @BindView(R.id.dalker_detail_photo)
    CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dalker);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        User user = bundle.getParcelable(getString(R.string.dalker_intent_detail));
        Timber.d(user.toString());


        Picasso.get()
                .load(user.getPictureURL().getLarge())
                .error(R.mipmap.ic_launcher_foreground)
                .placeholder(R.mipmap.ic_launcher_foreground)
                .into(imageView);
    }


}
