package dalker.cmtruong.com.app.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.database.DalkerDatabase;
import dalker.cmtruong.com.app.helper.GlideApp;
import dalker.cmtruong.com.app.model.User;
import timber.log.Timber;

/**
 * IntentService to request Room Database
 *
 * @author davidetruong
 * @version 1.0
 * @since 2018 August 15
 */
public class FavoriteDalkerIService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteFactory(this.getApplicationContext(), intent);
    }

    class MyRemoteFactory implements RemoteViewsFactory {

        private List<User> users = new ArrayList<>();
        private Context mContext;
        private DalkerDatabase mDb;
        private int mAppWidgetId;


        public MyRemoteFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Timber.d("on Create Service");
        }

        @Override
        public void onCreate() {
            mDb = DalkerDatabase.getsInstance(mContext);
            users = mDb.userDAO().loadFavUser();
            Timber.d("List user: " + users.toString());
        }

        @Override
        public void onDataSetChanged() {
            users = mDb.userDAO().loadFavUser();
        }

        @Override
        public void onDestroy() {
            users.clear();
        }

        @Override
        public int getCount() {
            return users.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            final RemoteViews mRemoteView = new RemoteViews(mContext.getPackageName(), R.layout.dalker_wiget_row);
            if (users.size() > 0) {
                User user = users.get(position);
                Timber.d("List user: " + user.toString());
                mRemoteView.setTextViewText(R.id.name_fav, user.getName().getFirstName() + " " + user.getName().getLastName());
                StorageReference storageReference = null;
                if (user.getPictureURL() != null) {
                    storageReference = FirebaseStorage.getInstance().getReference(user.getPictureURL().getLarge());
                }
                try {
                    Bitmap bitmap = GlideApp.with(mContext).asBitmap().load(storageReference).submit().get();
                    mRemoteView.setImageViewBitmap(R.id.dalker_fav_photo, bitmap);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }


            }
            return mRemoteView;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
