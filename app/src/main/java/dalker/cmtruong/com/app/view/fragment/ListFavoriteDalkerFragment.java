package dalker.cmtruong.com.app.view.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.adapter.DalkerListAdapter;
import dalker.cmtruong.com.app.database.AppExecutors;
import dalker.cmtruong.com.app.database.DalkerDatabase;
import dalker.cmtruong.com.app.model.User;
import dalker.cmtruong.com.app.viewmodel.FavoriteDalkerViewModel;
import timber.log.Timber;

/**
 * Favorite Fragment
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
public class ListFavoriteDalkerFragment extends Fragment {

    private static final String TAG = ListFavoriteDalkerFragment.class.getSimpleName();

    @BindView(R.id.dalker_list_favorite_rv)
    RecyclerView favRV;

    @BindView(R.id.no_fav_tv)
    TextView noFavMessage;

    FavoriteDalkerViewModel viewModel;
    private DalkerListAdapter mAdapter;

    private DalkerDatabase mDB;

    public ListFavoriteDalkerFragment() {
    }

    public static ListFavoriteDalkerFragment getInstance() {
        ListFavoriteDalkerFragment fragment = new ListFavoriteDalkerFragment();
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        Timber.tag(TAG);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(FavoriteDalkerViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        ButterKnife.bind(this, view);
        Timber.d("Open favorite fragment");

        viewModel.getUsers().observe(this, users -> {
            if (users != null) {
                mAdapter = new DalkerListAdapter(users);
                Timber.d(users.toString());
                favRV.setLayoutManager(new LinearLayoutManager(getActivity()));
                favRV.setHasFixedSize(true);
                favRV.setAdapter(mAdapter);
                showDalkerList();
            } else {
                showMessageError();
            }
        });


//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                AppExecutors.getInstance().diskIO().execute(() -> {
//                    int position = viewHolder.getAdapterPosition();
//                    List<User> userList = mAdapter.getUsers();
//                    mDB.userDAO().deleteUser(userList.get(position));
//                });
//            }
//        }).attachToRecyclerView(favRV);

        mDB = DalkerDatabase.getsInstance(getActivity().getApplicationContext());
        return view;
    }

    private void showDalkerList() {
        favRV.setVisibility(View.VISIBLE);
        noFavMessage.setVisibility(View.GONE);
    }

    private void showMessageError() {
        favRV.setVisibility(View.GONE);
        noFavMessage.setVisibility(View.VISIBLE);
    }

}
