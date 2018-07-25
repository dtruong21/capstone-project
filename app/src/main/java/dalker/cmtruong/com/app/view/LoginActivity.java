package dalker.cmtruong.com.app.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dalker.cmtruong.com.app.R;

/**
 * Login activity for dalker app which requires the connection with account and password
 *
 * @author davidetruong
 * @version 1.0
 * @since JUly 25th, 2018
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
