package dalker.cmtruong.com.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 2018 August, 25th
 */
public class DalkerRequestService extends IntentService {

    String currentLocation;
    double mLatitude;
    double mLongitude;
    ArrayList<User> users;


    DatabaseReference mDB;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public DalkerRequestService() {
        super(DalkerRequestService.class.getSimpleName());
        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("Start intent service");
        if (intent != null) {
            currentLocation = intent.getStringExtra(getString(R.string.current_location));
            mLatitude = intent.getDoubleExtra(getString(R.string.latitude), 0.0d);
            mLongitude = intent.getDoubleExtra(getString(R.string.longitude), 0.0d);
            Timber.d("location: %s", currentLocation);
            getDataByLocation(currentLocation);
        } else {
            Timber.d("Error here");
        }

    }

    private void getDataByLocation(String location) {
        users = new ArrayList<>();

        mDB = FirebaseDatabase.getInstance().getReference(getString(R.string.users));
        mDB.orderByChild(getString(R.string.m_location_city)).equalTo(location)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                User user = data.getValue(User.class);
                                users.add(user);
                            }
                            sendUsersToUI(users);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Timber.d("Failed to get data");
                    }
                });
    }


    private void sendUsersToUI(ArrayList<User> users) {
        Intent intent = new Intent();
        intent.setAction(getString(R.string.user_list_location));
        if (users.size() > 0) {
            intent.putExtra(getString(R.string.user_list), users);
        }

        Timber.d("Checked: %s", users.toString());
        sendBroadcast(intent);
    }

}
