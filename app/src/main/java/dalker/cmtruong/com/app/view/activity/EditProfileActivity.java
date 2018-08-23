package dalker.cmtruong.com.app.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.view.fragment.EditProfileFragment;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 22th
 */
public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_edit_profile);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.edit_container, EditProfileFragment.getInstance())
                .addToBackStack(null)
                .commit();
    }
}
