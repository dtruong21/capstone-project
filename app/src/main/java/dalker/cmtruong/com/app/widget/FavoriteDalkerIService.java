package dalker.cmtruong.com.app.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import dalker.cmtruong.com.app.database.DalkerDatabase;
import dalker.cmtruong.com.app.model.User;

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

        }

        @Override
        public void onCreate() {
            users = DalkerDatabase.getsInstance(mContext).userDAO().loadFavUser();

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

            return null;
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
