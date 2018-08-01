package dalker.cmtruong.com.app.view.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.utilities.NetworkUtilities;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 1st August, 2018
 */
public class ListDalkerFragment extends Fragment {
    @BindView(R.id.dalker_list_rv)
    RecyclerView mDalkerRV;

    @BindView(R.id.dalker_list_pb)
    ProgressBar mProgressBar;

    @BindView(R.id.dalker_search_error)
    TextView mErrorMessage;


    private static final String TAG = ListDalkerFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
    }

    public ListDalkerFragment() {
    }

    public static ListDalkerFragment getInstance() {
        ListDalkerFragment fragment = new ListDalkerFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_dalker, container, false);
        setRetainInstance(true);
        ButterKnife.bind(this, view);
        Timber.d("Fragment Demo ListDalker is created");
        initData();
        return view;
    }

    private void initData() {
        makeFakeUserQuery();
    }

    private void showDalkerList() {
        mDalkerRV.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void showMessageError() {
        mDalkerRV.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void waitingForResult() {
        mDalkerRV.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.GONE);
    }

    private void makeFakeUserQuery() {
        String mQuery = getString(R.string.url_fake_user);
        URL mUrl = NetworkUtilities.buildURL(mQuery);
        new FakeUserAsyncTask().execute(mUrl);
    }

    /**
     * @author davidetruong
     * @version 1.0
     * @since 1st August, 2018
     */
    private class FakeUserAsyncTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitingForResult();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            Timber.d(url.toString());
            String mQueryUrl = null;
            try {
                mQueryUrl = NetworkUtilities.getResultFromHttp(url);
                Timber.d(mQueryUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mQueryUrl;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && !s.equals("")) {
                JSONObject jsonObject;
                JSONArray mArray = null;
                List<User> users = null;
                try {
                    jsonObject = new JSONObject(s);
                    mArray = jsonObject.getJSONArray("results");
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<User>>() {
                    }.getType();
                    users = gson.fromJson(mArray.toString(), type);
                    Timber.d("My results: %s", users.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                showDalkerList();
            } else {
                showMessageError();
            }
        }
    }
}
