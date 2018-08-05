package dalker.cmtruong.com.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import dalker.cmtruong.com.app.database.DalkerDatabase;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

/**
 * @author davidetruong
 * @version 1.0
 * @since 05 August 2018
 */
public class FavoriteDalkerViewModel extends AndroidViewModel {

    private static final String TAG = FavoriteDalkerViewModel.class.getSimpleName();

    private LiveData<List<User>> users;

    public FavoriteDalkerViewModel(@NonNull Application application) {
        super(application);
        Timber.tag(TAG);
        DalkerDatabase mDB = DalkerDatabase.getsInstance(this.getApplication());
        Timber.d("Actively retrieving users from the DataBase");
    }

    public LiveData<List<User>> getUsers() {
        return users;
    }
}
