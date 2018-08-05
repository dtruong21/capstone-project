package dalker.cmtruong.com.app.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import dalker.cmtruong.com.app.database.DalkerDatabase;

/**
 * @author davidetruong
 * @version 1.0
 * @since 05 August 2018
 */
public class AddFavoriteDalkerVMFactory extends ViewModelProvider.NewInstanceFactory {

    private final DalkerDatabase mDb;

    private final int dalkerId;

    public AddFavoriteDalkerVMFactory(DalkerDatabase mDb, int dalkerId) {
        this.mDb = mDb;
        this.dalkerId = dalkerId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddFavoriteDalkerViewModel(mDb, dalkerId);
    }
}
