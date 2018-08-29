package dalker.cmtruong.com.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    FirebaseFirestore mFF;

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
        }

    }

    private void getDataByLocation(String location) {
        mFF = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        mFF.collection(getString(R.string.users))
                .whereEqualTo(getString(R.string.location_city), location)
                .get()
                .addOnFailureListener(e -> Timber.d("Failed to get data"))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.isComplete()) {
                        Timber.d("Size: " + task.getResult().getDocuments());
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(documentSnapshot.getData());
                            User user = gson.fromJson(jsonElement, User.class);
                            users.add(user);
                        }
                        sendUsersToUI(users);
                    }
                });
    }


    private void sendUsersToUI(ArrayList<User> users) {
        Intent intent = new Intent();
        intent.setAction(getString(R.string.user_list_location));
        intent.putExtra(getString(R.string.user_list), users);
        Timber.d("Checked: %s", users.toString());
        sendBroadcast(intent);
    }

}
