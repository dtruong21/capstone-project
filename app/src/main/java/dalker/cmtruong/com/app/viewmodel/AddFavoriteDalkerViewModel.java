package dalker.cmtruong.com.app.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import dalker.cmtruong.com.app.database.DalkerDatabase;
import dalker.cmtruong.com.app.model.User;

/**
 * @author davidetruong
 * @version 1.0
 * @since 05 August 2018
 */
public class AddFavoriteDalkerViewModel extends ViewModel {

    private LiveData<User> user;

    public AddFavoriteDalkerViewModel(DalkerDatabase mDB, int dalkerId) {
        user = mDB.userDAO().loadUserById(dalkerId);
    }

    public LiveData<User> getUser() {
        return user;
    }
}
