package dalker.cmtruong.com.app.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

/**
 * Dalker database class
 *
 * @author davidetruong
 * @version 1.0
 * @since 31th July, 2018
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DalkerDatabase extends RoomDatabase {
    private static final String TAG = DalkerDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "dalker";
    private static final Object LOCK = new Object();

    private static DalkerDatabase sInstance;

    private static DalkerDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Timber.d("Create new database instance: %s", DATABASE_NAME);
                sInstance = Room.databaseBuilder(context.getApplicationContext(), DalkerDatabase.class, DalkerDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Timber.d("Getting the database instance");
        return sInstance;
    }

    public abstract UserDAO userDAO();
}
