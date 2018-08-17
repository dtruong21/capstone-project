package dalker.cmtruong.com.app.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import dalker.cmtruong.com.app.R;
import dalker.cmtruong.com.app.database.DalkerDatabase;
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

    private static final String TAG = FavoriteDalkerIService.class.getSimpleName();

    public FavoriteDalkerIService() {
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new DalkerRemoteViewFactory(this.getApplicationContext(), intent);
    }

    public class DalkerRemoteViewFactory implements RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;
        private List<User> users;

        public DalkerRemoteViewFactory(Context mContext, Intent intent) {
            this.mContext = mContext;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Timber.d("Create new Service factory");
        }

        @Override
        public void onCreate() {
            users = DalkerDatabase.getsInstance(mContext).userDAO().loadFavUser();
            Timber.d(users.toString());
        }

        @Override
        public void onDataSetChanged() {
            users = DalkerDatabase.getsInstance(mContext).userDAO().loadFavUser();
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (users == null)
                return 0;
            return users.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rm = new RemoteViews(mContext.getPackageName(), R.layout.dalker_wiget_row);

            User user = users.get(position);
            String name = user.getName().getFirstName() + " " + user.getName().getLastName();
            rm.setTextViewText(R.id.name_fav, name);
            Picasso.get()
                    .load(user.getPictureURL().getLarge())
                    .into(rm, R.id.dalker_fav_photo, new int[]{mAppWidgetId});
            return rm;
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
