package dalker.cmtruong.com.app.widget;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import dalker.cmtruong.com.app.viewmodel.FavoriteDalkerViewModel;

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
        return new RemoteViewsFactory() {


            @Override
            public void onCreate() {


            }

            @Override
            public void onDataSetChanged() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return 0;
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
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
